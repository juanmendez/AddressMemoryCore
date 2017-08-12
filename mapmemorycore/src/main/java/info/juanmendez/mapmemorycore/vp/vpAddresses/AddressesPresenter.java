package info.juanmendez.mapmemorycore.vp.vpAddresses;

import javax.inject.Inject;

import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.vp.ViewPresenter;
import io.realm.RealmResults;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class AddressesPresenter implements ViewPresenter<AddressesPresenter, AddressesFragment>{

    @Inject
    AddressProvider addressProvider;

    public static final String TAG = "addressesView";

    private RealmResults<ShortAddress> addresses;
    private AddressesFragment view;

    public AddressesPresenter() {
        MapCoreModule.getComponent().inject(this);
    }

    @Override
    public AddressesPresenter register(AddressesFragment view) {
        this.view = view;
        view.injectAddresses( addressProvider.getAddresses() );

        return this;
    }

    @Override
    public void active(String action) {

    }

    @Override
    public void inactive(){

    }

    public void selectAddress( ShortAddress address ){
        addressProvider.selectAddress( address );
    }

    public void addAddress(){
        addressProvider.selectAddress( new ShortAddress() );
    }
}
