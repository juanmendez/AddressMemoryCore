package info.juanmendez.mapmemorycore.vp.vpAddresses;

import javax.inject.Inject;

import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.services.AddressProvider;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class AddressesPresenter{

    @Inject
    AddressProvider provider;

    public AddressesPresenter() {
        MapCoreModule.getComponent().inject(this);
    }

    public AddressProvider getProvider() {
        return provider;
    }
}
