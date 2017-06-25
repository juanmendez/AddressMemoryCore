package info.juanmendez.mapmemorycore.vp.vpAddresses;

import javax.inject.Inject;

import info.juanmendez.mapmemorycore.dependencies.RealmProvider;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class AddressesPresenter{

    @Inject
    RealmProvider realmProvider;

    public AddressesPresenter() {
        MapCoreModule.getComponent().inject(this);
    }

    public RealmProvider getProvider() {
        return realmProvider;
    }
}
