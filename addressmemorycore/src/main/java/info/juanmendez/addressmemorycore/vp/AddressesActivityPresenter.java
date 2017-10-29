package info.juanmendez.addressmemorycore.vp;

import javax.inject.Inject;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.modules.MapModuleBase;

/**
 * Created by Juan Mendez on 10/28/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class AddressesActivityPresenter {

    @Inject
    AddressProvider addressProvider;

    public AddressesActivityPresenter() {
        MapModuleBase.getInjector().inject(this);
    }

    public void onResume(){
        addressProvider.connect();
    }

    public void onPause(){
        addressProvider.disconnect();
    }
}
