package info.juanmendez.mapmemorycore.mamemorycore;

import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.DroidAddressProvider;
import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * So we can also test with Realm and instead of using TestApp, we can
 * provide to the Dagger module this instance which of course comes with a Realm object
 */
public class TestRealmApp extends TestApp {

    Realm realm;

    public TestRealmApp() {
        super();

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
    public AddressProvider getAddressProvider() {
        return new DroidAddressProvider(application, realm);
    }
}