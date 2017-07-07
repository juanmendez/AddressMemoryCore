package info.juanmendez.mapmemorycore.dependencies;

import io.realm.Realm;

/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface RealmProvider {
    public Realm getRealm();
}
