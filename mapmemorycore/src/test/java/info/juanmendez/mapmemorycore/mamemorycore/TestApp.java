package info.juanmendez.mapmemorycore.mamemorycore;

import android.app.Application;

import org.mockito.Mockito;

import info.juanmendez.mapmemorycore.CoreApp;
import info.juanmendez.mapmemorycore.dependencies.AddressService;
import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.NavigationService;
import info.juanmendez.mapmemorycore.dependencies.NetworkService;
import info.juanmendez.mapmemorycore.dependencies.PhotoService;
import info.juanmendez.mapmemorycore.mamemorycore.dependencies.TestAddressProvider;

import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;


/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestApp implements CoreApp {

    protected Application application;

    public TestApp() {
        application = Mockito.mock( Application.class );

        doAnswer(invocation -> {
            return "Mocked Error Message " + invocation.getArgumentAt(0, Integer.class ).toString();
        }).when( application ).getString( Mockito.anyInt() );
    }

    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public AddressProvider getAddressProvider() {
        return new TestAddressProvider();
    }

    @Override
    public AddressService getAddressService() {
        return mock( AddressService.class );
    }

    @Override
    public NetworkService getNetworkService() {
        return mock( NetworkService.class );
    }

    @Override
    public PhotoService getPhotoService() {
        return mock( PhotoService.class );
    }

    @Override
    public NavigationService getNavigationService() {
        return mock(NavigationService.class);
    }
}
