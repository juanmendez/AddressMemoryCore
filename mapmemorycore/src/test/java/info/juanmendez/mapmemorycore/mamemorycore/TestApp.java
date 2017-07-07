package info.juanmendez.mapmemorycore.mamemorycore;

import android.app.Application;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import info.juanmendez.mapmemorycore.CoreApp;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AutocompleteService;
import info.juanmendez.mapmemorycore.dependencies.db.AddressProvider;
import info.juanmendez.mapmemorycore.mamemorycore.dependencies.TestAddressProvider;
import info.juanmendez.mapmemorycore.mamemorycore.dependencies.TestAutocompleteService;


/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestApp implements CoreApp {

    Application application;

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
    public AutocompleteService getAutocomplete() {
        return new TestAutocompleteService(application);
    }
}
