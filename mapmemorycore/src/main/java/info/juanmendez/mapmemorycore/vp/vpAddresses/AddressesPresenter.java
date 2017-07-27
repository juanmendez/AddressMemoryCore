package info.juanmendez.mapmemorycore.vp.vpAddresses;

import javax.inject.Inject;

import info.juanmendez.mapmemorycore.dependencies.Response;
import info.juanmendez.mapmemorycore.dependencies.db.AddressProvider;
import info.juanmendez.mapmemorycore.models.MapAddress;
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

    private RealmResults<MapAddress> addresses;
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

    @Override
    public void submitAddress(Response<MapAddress> response) {

    }

    public void selectAddress( MapAddress address ){
        addressProvider.selectAddress( address );
    }

    public void addAddress(){
        addressProvider.selectAddress( null );
    }



}
