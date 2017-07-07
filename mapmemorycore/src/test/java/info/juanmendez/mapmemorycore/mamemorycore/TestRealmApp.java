package info.juanmendez.mapmemorycore.mamemorycore;

import android.app.Application;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import info.juanmendez.mapmemorycore.CoreApp;
import info.juanmendez.mapmemorycore.dependencies.db.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.db.RealmAddressProvider;
import info.juanmendez.mapmemorycore.mamemorycore.dependencies.TestAutocompleteService;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AutocompleteService;
import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestRealmApp implements CoreApp {

    Application application;
    Realm realm;

    public TestRealmApp() {
        application = Mockito.mock( Application.class );
        PowerMockito.doAnswer(invocation -> {
            
            return "Mocked Error Message " + invocation.getArgumentAt(0, Integer.class ).toString();
        }).when( application ).getString( Mockito.anyInt() );

        Realm.init( application );
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder()
                .name("info.juanmendez.mapmemorycore")
                .build();

        Realm.deleteRealm(realmConfiguration);
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public AddressProvider getAddressProvider() {
        return new RealmAddressProvider(application, realm);
    }

    @Override
    public AutocompleteService getAutocomplete() {
        return new TestAutocompleteService(application);
    }
}
