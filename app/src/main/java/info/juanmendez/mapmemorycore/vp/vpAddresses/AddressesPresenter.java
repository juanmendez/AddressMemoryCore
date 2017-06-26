package info.juanmendez.mapmemorycore.vp.vpAddresses;

import javax.inject.Inject;

import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import io.realm.RealmResults;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class AddressesPresenter{

    @Inject
    AddressProvider addressProvider;

    RealmResults<Address> addresses;

    public AddressesPresenter() {
        MapCoreModule.getComponent().inject(this);
    }
}
