import android.app.Application;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.cloud.Auth;
import info.juanmendez.addressmemorycore.dependencies.cloud.AuthService;
import info.juanmendez.addressmemorycore.dependencies.cloud.Syncronizer;
import info.juanmendez.addressmemorycore.modules.CloudCoreModule;
import info.juanmendez.addressmemorycore.vp.vpAuth.AuthPresenter;
import info.juanmendez.addressmemorycore.vp.vpAuth.AuthView;
import info.juanmendez.addressmemorycore.vp.vpAuth.AuthViewModel;
import info.juanmendez.mapmemorycore.addressmemorycore.dependencies.TestAddressProvider;
import info.juanmendez.mapmemorycore.addressmemorycore.dependencies.TwistAuth;
import info.juanmendez.mapmemorycore.addressmemorycore.dependencies.TwistNetworkService;
import info.juanmendez.mapmemorycore.addressmemorycore.dependencies.TwistSyncronizer;
import io.reactivex.disposables.CompositeDisposable;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by juan on 1/8/18.
 */

public class TestCloud extends TestAddressMemoryCore{

    private CloudCoreModule mCloudModule;
    private AuthService mAuthService;
    private AddressProvider mAddressProvider;
    private Syncronizer mSyncronizer;

    private TwistAuth mTwistAuth;
    private TwistSyncronizer mTwistSyncronizer;
    private TwistNetworkService mTwistNetworkService;


    @Before
    public void before(){
        Application application = Mockito.mock( Application.class );

        doAnswer(invocation -> {
            return "Mocked Error Message " + invocation.getArgumentAt(0, Integer.class ).toString();
        }).when( application ).getString( Mockito.anyInt() );

        mAddressProvider = spy(new TestAddressProvider());

        Auth auth = mock(Auth.class);
        mAuthService = new AuthService(auth);
        mTwistAuth = new TwistAuth( auth, mAuthService );

        mSyncronizer = mock(Syncronizer.class);
        mTwistSyncronizer = new TwistSyncronizer(mSyncronizer, mAddressProvider );
        mTwistNetworkService = new TwistNetworkService( mock(NetworkService.class));

        mCloudModule = new CloudCoreModule( application );
        mCloudModule.applyAuthService( mAuthService );
        mCloudModule.applySyncronizer(mSyncronizer);
        mCloudModule.applyNetworkService( mTwistNetworkService.getNetworkService() );
    }

    @Test
    public void testLogin(){

        assertFalse( mAuthService.isLoggedIn() );

        mTwistAuth.setLoggedIn(true);
        assertTrue( mAuthService.isLoggedIn());


        if( !mSyncronizer.isSynced() ){
            mSyncronizer.pushToTheCloud().subscribe(
                    aBoolean -> {
                        assertTrue( aBoolean);
                        assertTrue( mSyncronizer.isSynced() );
                        },
                    throwable -> {});
        }


        mTwistAuth.setLoggedIn(false);
        mTwistSyncronizer.setException( new Exception("Some error"));

        mSyncronizer.pushToTheCloud().subscribe(
                aBoolean -> { throw new Exception("not good"); },
                throwable -> { assertNotNull( throwable );});

        assertFalse( mSyncronizer.isSynced() );
    }


    @Test
    public void testPresenter(){

        AuthPresenter authPresenter = new AuthPresenter( mCloudModule );

        //lets see if we are doing the job right
        AuthView view = mock( AuthView.class );
        AuthViewModel viewModel = authPresenter.getViewModel( view );


        mTwistAuth.login( view );
        authPresenter.active(null);
        assertTrue( viewModel.loggedIn.get() );

        mTwistAuth.logout(view);
        assertFalse( viewModel.loggedIn.get() );

        authPresenter.inactive(true);

        mTwistAuth.login( view );
        authPresenter.active(null);
        authPresenter.inactive(true);
        authPresenter.active(null);
        assertTrue( viewModel.loggedIn.get() );

        //lets rotate
        mTwistAuth.logout(view);
        authPresenter.inactive(true);
        authPresenter.active(null);
        assertFalse( viewModel.loggedIn.get() );
    }

    @Test
    public void testPushingToTheCloud(){

        AuthPresenter authPresenter = new AuthPresenter( mCloudModule );

        //lets see if we are doing the job right
        AuthView view = mock( AuthView.class );
        AuthViewModel viewModel = authPresenter.getViewModel( view );

        mTwistAuth.login( view );
        authPresenter.active(null);

        //lets check if presenter tried to push to the cloud
        Mockito.verify(mSyncronizer, Mockito.times(1) ).pushToTheCloud();

        mTwistAuth.logout(view); //1


        mTwistAuth.login( view );
        authPresenter.inactive(true);

        //even if we rotate, we are still logged in.
        assertTrue( mAuthService.isLoggedIn() );
        CompositeDisposable compositeDisposable = Whitebox.getInternalState( authPresenter, "mComposite");
        assertTrue( compositeDisposable.isDisposed() );

        mSyncronizer.setSynced( false );

        mTwistSyncronizer.setException( new Exception("Firebase can't import realm data!!"));

        //after rotation..
        authPresenter.active(null);

        assertTrue( mAuthService.isLoggedIn() );
        assertFalse( mSyncronizer.isSynced() );
        assertTrue( viewModel.loggedIn.get() );

        mTwistAuth.logout(view);//2
        assertFalse( viewModel.loggedIn.get() );
        Mockito.verify( mAddressProvider, Mockito.times(2) ).deleteAddresses();
    }

    /**
     * Making sure we don't deleteAddresses if we never logged in..
     */
    @Test
    public void testAddressesDeleted(){


        AuthPresenter authPresenter = new AuthPresenter( mCloudModule );

        //lets see if we are doing the job right
        AuthView view = mock( AuthView.class );
        AuthViewModel viewModel = authPresenter.getViewModel( view );

        authPresenter.active(null);
        Mockito.verify( mAddressProvider, Mockito.times(0) ).connect();

        Mockito.verify( mAddressProvider, Mockito.times(1) ).deleteAddresses();
        authPresenter.inactive(true);
        Mockito.verify( mAddressProvider, Mockito.times(1) ).deleteAddresses();
    }


    @Test
    public void testNetworkAgainstLogin(){

        AuthPresenter authPresenter = new AuthPresenter( mCloudModule );
        NetworkService networkService = mCloudModule.getNetworkService();

        //lets see if we are doing the job right
        AuthView view = mock( AuthView.class );
        AuthViewModel viewModel = authPresenter.getViewModel( view );

        authPresenter.active(null);
        assertTrue( viewModel.loginWhenOnline.get()  );

        //because we are not logged in, then beforeLogin is not invoked!
        verify( view, times(0)).beforeLogin();

        //ok, lets go back online!!
        mTwistNetworkService.setConnected( true );

        //we show the firebaseLogin screen
        verify( view, times(1)).beforeLogin();

        //we are going offline :)
        mTwistNetworkService.setConnected( false );
        assertTrue( viewModel.loginWhenOnline.get()  );
        verify( view, times(1)).beforeLogin();

        authPresenter.inactive(true);

        authPresenter.active(null);
        assertTrue( viewModel.loginWhenOnline.get()  );
        verify( view, times(1)).beforeLogin();

        //we are going to go online once again
        mTwistNetworkService.setConnected( true );
        assertFalse( viewModel.loginWhenOnline.get()  );
        verify( view, times(2)).beforeLogin();

        //lets login
        authPresenter.inactive(true);
        mTwistAuth.login( view );
        authPresenter.active(null);
        assertFalse( viewModel.loginWhenOnline.get()  );
        verify( view, times(2)).beforeLogin();
        verify( view, times(1)).afterLogin();

    }
}
