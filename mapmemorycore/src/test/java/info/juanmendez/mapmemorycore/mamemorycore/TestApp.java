package info.juanmendez.mapmemorycore.mamemorycore;

import android.app.Application;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import info.juanmendez.mapmemorycore.CoreApp;
import info.juanmendez.mapmemorycore.dependencies.Navigation;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AddressService;
import info.juanmendez.mapmemorycore.dependencies.db.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.network.NetworkService;
import info.juanmendez.mapmemorycore.mamemorycore.dependencies.TestAddressProvider;

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

        PowerMockito.doAnswer(invocation -> {
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
    public NetworkService getNeworkService() {
        return mock( NetworkService.class );
    }

    @Override
    public Navigation getNavigation() {
        return mock( Navigation.class );
    }
}
