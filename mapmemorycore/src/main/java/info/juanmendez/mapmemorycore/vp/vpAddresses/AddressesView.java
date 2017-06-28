package info.juanmendez.mapmemorycore.vp.vpAddresses;

import info.juanmendez.mapmemorycore.models.Address;
import io.realm.RealmResults;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AddressesView {
    void injectAddresses(RealmResults<Address> addresses );
}
