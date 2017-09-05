package info.juanmendez.mapmemorycore.vp.vpAddresses;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.utils.ModelUtils;

/**
 * Created by Juan Mendez on 9/4/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class AddressesProxy {

    @Inject
    AddressProvider addressProvider;
    private List<ShortAddress> addresses = new ArrayList<>();

    public AddressesProxy() {
        MapCoreModule.getComponent().inject(this);
    }

    /**
     * Since widgets run on their own thread, we can get an exception
     * due to accessing realm objects outside the main thread.
     * What we can do is clone the objects. :)
     * @return a list with ShortAddreses free from Realm
     */
    public List<ShortAddress> getAddresses() {
        List<ShortAddress> realmAddresses = addressProvider.getAddresses();
        addresses.clear();

        for( ShortAddress shortAddress: realmAddresses ){
            addresses.add(ModelUtils.cloneAddress(shortAddress));
        }

        return addresses;
    }
}
