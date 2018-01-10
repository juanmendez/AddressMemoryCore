import android.app.Activity;
import android.app.Application;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.cloud.Auth;
import info.juanmendez.addressmemorycore.dependencies.cloud.AuthService;
import info.juanmendez.addressmemorycore.dependencies.cloud.CloudStation;
import info.juanmendez.addressmemorycore.dependencies.cloud.CloudSyncronizer;
import info.juanmendez.addressmemorycore.modules.CloudCoreModule;
import info.juanmendez.addressmemorycore.vp.vpAuth.AuthPresenter;
import info.juanmendez.addressmemorycore.vp.vpAuth.AuthView;
import info.juanmendez.addressmemorycore.vp.vpAuth.AuthViewModel;
import info.juanmendez.mapmemorycore.addressmemorycore.dependencies.TestAddressProvider;
import info.juanmendez.mapmemorycore.addressmemorycore.dependencies.TwistAuth;
import info.juanmendez.mapmemorycore.addressmemorycore.dependencies.TwistAuthView;
import info.juanmendez.mapmemorycore.addressmemorycore.dependencies.TwistCloudStation;
import info.juanmendez.mapmemorycore.addressmemorycore.dependencies.TwistCloudSyncronizer;
import io.reactivex.disposables.CompositeDisposable;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by juan on 1/8/18.
 */

public class TestCloud extends TestAddressMemoryCore{

    private CloudCoreModule mCloudModule;
    private AuthService mAuthService;
    private AddressProvider mAddressProvider;
    private CloudSyncronizer mCloudSyncronizer;

    private TwistAuth mTwistAuth;
    private TwistCloudStation mTwistCloudStation;
    private TwistCloudSyncronizer mTwistCloudSyncronizer;


    @Before
    public void before(){
        Application application = Mockito.mock( Application.class );

        doAnswer(invocation -> {
            return "Mocked Error Message " + invocation.getArgumentAt(0, Integer.class ).toString();
        }).when( application ).getString( Mockito.anyInt() );

        mAddressProvider = new TestAddressProvider();

        mTwistAuth = new TwistAuth( mock(Auth.class) );
        mAuthService = new AuthService(mTwistAuth.getAuth());
        mTwistCloudStation = new TwistCloudStation( mock(CloudStation.class), mAuthService );

        mCloudSyncronizer = mock(CloudSyncronizer.class);
        mTwistCloudSyncronizer = new TwistCloudSyncronizer( mCloudSyncronizer );

        mCloudModule = new CloudCoreModule( application );
        mCloudModule.applyAddressProvider( mAddressProvider );
        mCloudModule.applyAuthService( mAuthService );
        mCloudModule.applyCloudSyncronizer( mCloudSyncronizer );
    }

    @Test
    public void testLogin(){

        assertFalse( mAuthService.isLoggedIn() );

        mCloudSyncronizer.countAddresses().subscribe(result -> {
            assertTrue( result== 0);
        });


        mAuthService.onLoginResponse( AuthService.FB_SESSION, Activity.RESULT_OK, null );
        assertTrue( mAuthService.isLoggedIn());


        if( !mCloudSyncronizer.isSynced() ){
            mCloudSyncronizer.pushToTheCloud( mAddressProvider.getAddresses() ).subscribe(
                    aBoolean -> {
                        assertTrue( aBoolean);
                        assertTrue( mCloudSyncronizer.isSynced() );
                        },
                    throwable -> {});
        }



        mTwistCloudSyncronizer.setException( new Exception("Some error"));

        mCloudSyncronizer.pushToTheCloud( mAddressProvider.getAddresses() ).subscribe(
                aBoolean -> { throw new Exception("not good"); },
                throwable -> { assertNotNull( throwable );});

        assertFalse( mCloudSyncronizer.isSynced() );
    }


    @Test
    public void testPresenter(){

        AuthPresenter authPresenter = new AuthPresenter( mCloudModule );

        //lets see if we are doing the job right
        AuthView view = mock( AuthView.class );
        AuthViewModel viewModel = authPresenter.getViewModel( view );

        TwistAuthView twistAuthView = new TwistAuthView( view, authPresenter, mAuthService );
        twistAuthView.setEnableAccess(true);

        authPresenter.active(null);
        mAuthService.login( view );
        assertTrue( viewModel.loggedIn.get() );

        mAuthService.logout();
        assertFalse( viewModel.loggedIn.get() );

        mAuthService.login( view );
        assertTrue( viewModel.loggedIn.get() );

        //lets rotate
        authPresenter.inactive(true);
        mAuthService.logout();
        authPresenter.active(null);
        assertFalse( viewModel.loggedIn.get() );
    }

    @Test
    public void testPushingToTheCloud(){

        AuthPresenter authPresenter = new AuthPresenter( mCloudModule );

        //lets see if we are doing the job right
        AuthView view = mock( AuthView.class );
        AuthViewModel viewModel = authPresenter.getViewModel( view );

        TwistAuthView twistAuthView = new TwistAuthView( view, authPresenter, mAuthService );
        twistAuthView.setEnableAccess(true);

        authPresenter.active(null);
        mAuthService.login( view );

        //lets check if presenter tried to push to the cloud
        Mockito.verify( mCloudSyncronizer, Mockito.times(1) ).pushToTheCloud( Mockito.anyList() );

        mAuthService.logout();


        mAuthService.login( view );
        authPresenter.inactive(true);

        //even if we rotate, we are still logged in.
        assertTrue( mAuthService.isLoggedIn() );
        CompositeDisposable compositeDisposable = Whitebox.getInternalState( authPresenter, "mComposite");
        assertTrue( compositeDisposable.isDisposed() );

        mCloudSyncronizer.setSynced( false );

        mTwistCloudSyncronizer.setException( new Exception("Firebase can't import realm data!!"));

        //after rotation..
        authPresenter.active(null);
        view.tryLogin();

        assertTrue( mAuthService.isLoggedIn() );
        assertFalse( mCloudSyncronizer.isSynced() );
        assertTrue( viewModel.loggedIn.get() );

        mAuthService.logout();
        assertFalse( viewModel.loggedIn.get() );

    }
}
