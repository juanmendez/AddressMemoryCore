import android.app.Application;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.QuickResponse;
import info.juanmendez.addressmemorycore.dependencies.cloud.AuthService;
import info.juanmendez.addressmemorycore.dependencies.cloud.CloudSyncronizer;
import info.juanmendez.addressmemorycore.modules.CloudCoreModule;
import info.juanmendez.mapmemorycore.addressmemorycore.dependencies.TestAddressProvider;
import info.juanmendez.mapmemorycore.addressmemorycore.dependencies.TwistAuthService;
import info.juanmendez.mapmemorycore.addressmemorycore.dependencies.TwistCloudSyncronizer;

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
}
