import android.app.Application;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.QuickResponse;
import info.juanmendez.addressmemorycore.dependencies.cloud.AuthService;
import info.juanmendez.addressmemorycore.dependencies.cloud.CloudSyncronizer;
import info.juanmendez.addressmemorycore.modules.CloudCoreModule;
import info.juanmendez.addressmemorycore.vp.vpAuth.AuthPresenter;
import info.juanmendez.addressmemorycore.vp.vpAuth.AuthView;
import info.juanmendez.addressmemorycore.vp.vpAuth.AuthViewModel;
import info.juanmendez.mapmemorycore.addressmemorycore.dependencies.TestAddressProvider;
import info.juanmendez.mapmemorycore.addressmemorycore.dependencies.TwistAuthService;
import info.juanmendez.mapmemorycore.addressmemorycore.dependencies.TwistCloudSyncronizer;
import io.reactivex.disposables.Disposable;

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


    @Before
    public void before(){
        Application application = Mockito.mock( Application.class );

        doAnswer(invocation -> {
            return "Mocked Error Message " + invocation.getArgumentAt(0, Integer.class ).toString();
        }).when( application ).getString( Mockito.anyInt() );


        mCloudModule = new CloudCoreModule( application );
        mCloudModule.applyAddressProvider( new TestAddressProvider() );
        mCloudModule.applyAuthService( mock(AuthService.class) );
        mCloudModule.applyCloudSyncronizer( mock(CloudSyncronizer.class));


        mAddressProvider = mCloudModule.getAddressProvider();
        mAuthService = mCloudModule.getAuthService();
        mCloudSyncronizer = mCloudModule.getCloudSyncronizer();
    }

    @Test
    public void testLogin(){
        TwistAuthService twistAuthService = new TwistAuthService( mAuthService );
        TwistCloudSyncronizer twistCloudSyncronizer = new TwistCloudSyncronizer( mCloudSyncronizer );

        assertFalse( mAuthService.isLoggedIn() );
        assertEquals( mCloudSyncronizer.countAddresses(), 0);

        twistAuthService.setLoggedIn( true );
        assertTrue( mAuthService.isLoggedIn());

        /**
         * ok, so when logged in, find out if user's data is in the cloud already.
         * the cloud should return some data..
         */
        if( !mCloudSyncronizer.isSynced() ){
            mCloudSyncronizer.pushToTheCloud( mAddressProvider.getAddresses() ).subscribe(
                    aBoolean -> {
                        assertTrue( aBoolean);
                        assertTrue( mCloudSyncronizer.isSynced() );
                        },
                    throwable -> {});
        }

        /**
         * how about something went wrong..
         */
        twistCloudSyncronizer.setException( new Exception("Some error"));

        mCloudSyncronizer.pushToTheCloud( mAddressProvider.getAddresses() ).subscribe(
                aBoolean -> { throw new Exception("not good"); },
                throwable -> { assertNotNull( throwable );});

        assertFalse( mCloudSyncronizer.isSynced() );
    }


    @Test
    public void testPresenter(){
        TwistAuthService twistAuthService = new TwistAuthService( mAuthService );
        TwistCloudSyncronizer twistCloudSyncronizer = new TwistCloudSyncronizer( mCloudSyncronizer );


        AuthPresenter authPresenter = new AuthPresenter( mCloudModule );

        //lets see if we are doing the job right
        AuthView view = mock( AuthView.class );
        AuthViewModel viewModel = authPresenter.getViewModel( view );
        authPresenter.active(null);

        //lets check if presenter tried to push to the cloud
        Mockito.verify( mCloudSyncronizer, Mockito.times(0) ).pushToTheCloud( Mockito.anyList() );

        //lets login!
        authPresenter.login();
        Mockito.verify( mCloudSyncronizer, Mockito.times(1) ).pushToTheCloud( Mockito.anyList() );
        authPresenter.logout();

        //now login won't call to push
        authPresenter.login();
        Mockito.verify( mCloudSyncronizer, Mockito.times(1) ).pushToTheCloud( Mockito.anyList() );
        authPresenter.logout();

        //login twice, means we call this method twice
        Mockito.verify( view, Mockito.times(2)).onAuthSuccess();

        //lets ensure its disposable is unregistered.
        Disposable disposable = Whitebox.getInternalState( authPresenter, "mPushDisposable");
        assertNotNull( disposable );
        assertTrue( disposable.isDisposed());


        //lets repeat, this time we want to fire an exception from cloudSyncronizer when pushing..
        mCloudSyncronizer.setSynced( false );
        twistCloudSyncronizer.setException( new Exception("Firebase can't import realm data!!"));
        authPresenter.login();

        Mockito.verify( view, Mockito.times(3)).onAuthSuccess();
        assertFalse( mCloudSyncronizer.isSynced() );
        assertTrue( viewModel.loggedIn.get() );

        authPresenter.logout();
        assertFalse( viewModel.loggedIn.get() );
    }



}
