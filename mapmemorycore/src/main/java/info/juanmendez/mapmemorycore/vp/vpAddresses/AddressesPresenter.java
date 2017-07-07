package info.juanmendez.mapmemorycore.vp.vpAddresses;

import javax.inject.Inject;

import info.juanmendez.mapmemorycore.dependencies.db.AddressProvider;
import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.vp.ViewPresenter;
import io.realm.RealmResults;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class AddressesPresenter implements ViewPresenter<AddressesPresenter, AddressesView>{

    @Inject
    AddressProvider addressProvider;

    public static final String TAG = "addressesView";

    private RealmResults<Address> addresses;
    private AddressesView view;

    public AddressesPresenter() {
        MapCoreModule.getComponent().inject(this);
    }

    @Override
    public AddressesPresenter onStart(AddressesView view) {
        this.view = view;
        view.injectAddresses( addressProvider.getAddresses() );
        return this;
    }

    @Override
    public AddressesPresenter onPause() {
        return this;
    }

    public void selectAddress( Address address ){
        addressProvider.selectAddress( address );
    }

    public void addAddress(){
        addressProvider.selectAddress( null );
    }
}
