package info.juanmendez.mapmemorycore.mamemorycore.dependencies;

import android.app.Application;

import info.juanmendez.mapmemorycore.dependencies.RealmProvider;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class TestRealmProvider implements RealmProvider{

    Application application;
    Realm realm;

    public TestRealmProvider(Application application) {
        this.application = application;
    }

    @Override
    public Realm getRealm() {
        if( realm == null ){
            Realm.init( application );
            RealmConfiguration realmConfiguration = new RealmConfiguration
                    .Builder()
                    .name("info.juanmendez.mapmemorycore")
                    .build();

            Realm.deleteRealm(realmConfiguration);
            Realm.setDefaultConfiguration(realmConfiguration);
            realm = Realm.getDefaultInstance();
        }

        return realm;
    }
}
