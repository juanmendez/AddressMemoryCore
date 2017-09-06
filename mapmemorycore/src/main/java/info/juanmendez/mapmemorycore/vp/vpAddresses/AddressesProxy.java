package info.juanmendez.mapmemorycore.vp.vpAddresses;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.utils.ModelUtils;
import info.juanmendez.mapmemorycore.utils.RxUtils;
import rx.Subscription;

/**
 * Created by Juan Mendez on 9/4/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class AddressesProxy {

    @Inject
    AddressProvider addressProvider;

    private List<ShortAddress> addresses = new ArrayList<>();
    Subscription subscription;

    public AddressesProxy() {
        MapCoreModule.getComponent().inject(this);
        refresh();
    }

    private void refresh(){

        subscription = addressProvider.getAddressesAsync().subscribe(shortAddresses -> {
            addresses.clear();

            for( ShortAddress shortAddress: shortAddresses ){
                addresses.add(ModelUtils.cloneAddress(shortAddress));
            }
        });
    }

    public List<ShortAddress> getAddresses() {
        return addresses;
    }

    public void onDestroy() {
        RxUtils.unsubscribe( subscription );
    }
}