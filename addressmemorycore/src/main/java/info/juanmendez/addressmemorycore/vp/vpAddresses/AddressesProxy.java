package info.juanmendez.addressmemorycore.vp.vpAddresses;

import java.util.List;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.models.ShortAddress;

/**
 * Created by Juan Mendez on 9/4/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * This is a proxy used by widgetProvider in order to get all mAddresses.
 * Addresses are copies as RealmObjects throw an error if used outside the main thread.
 * And naturally Android Widgets don't run in the main thread.
 */
public class AddressesProxy {

    //TODO rename to mAddressProvider
    AddressProvider addressProvider;
    private List<ShortAddress> mAddresses;

    public AddressesProxy( AddressProvider addressProvider) {
        this.addressProvider = addressProvider;

        addressProvider.connect();
        mAddresses = addressProvider.getClonedAddresses();
        addressProvider.disconnect();
    }

    public List<ShortAddress> getAddresses() {
        return mAddresses;
    }

    public void onDestroy() {
    }
}