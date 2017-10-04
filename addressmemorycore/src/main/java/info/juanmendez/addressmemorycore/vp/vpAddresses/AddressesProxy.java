package info.juanmendez.addressmemorycore.vp.vpAddresses;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.utils.ModelUtils;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.modules.MapModuleBase;
import info.juanmendez.addressmemorycore.utils.RxUtils;
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
        MapModuleBase.getInjector().inject(this);
        refresh();
    }

    public void refresh(){
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