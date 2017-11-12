package info.juanmendez.addressmemorycore.vp;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;

/**
 * Created by Juan Mendez on 10/28/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class AddressesActivityPresenter {

    AddressProvider mAddressProvider;

    public AddressesActivityPresenter(AddressProvider addressProvider) {
        mAddressProvider = addressProvider;
    }

    public void onResume(){
        mAddressProvider.connect();
    }

    public void onPause(){
        mAddressProvider.disconnect();
    }
}
