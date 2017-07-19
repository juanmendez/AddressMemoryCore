package info.juanmendez.mapmemorycore.vp.vpAddress;

import java.util.List;

import javax.inject.Inject;

import info.juanmendez.mapmemorycore.dependencies.Navigation;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AddressResponse;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AddressService;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AddressesResponse;
import info.juanmendez.mapmemorycore.dependencies.db.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.network.NetworkService;
import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.vp.ViewPresenter;

/**
 * Created by Juan Mendez on 6/26/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class AddressPresenter implements ViewPresenter<AddressPresenter,AddressFragment>{

    @Inject
    AddressProvider addressProvider;

    @Inject
    AddressService addressService;

    @Inject
    NetworkService networkService;

    @Inject
    Navigation navigation;

    AddressFragment view;

    public static final String ADDRESS_VIEW_TAG = "viewAddressTag";
    public static final String ADDDRESS_EDIT_TAG = "editAddressTag";

    @Override
    public AddressPresenter register(AddressFragment view) {
        this.view = view;
        MapCoreModule.getComponent().inject(this);
        addressService.register( view.getActivity() );
        return this;
    }

    @Override
    public void active( String action ) {

        networkService.connect(available -> {
            view.onAddressResult( new Address(0), available );
        });

        addressService.onStart();
    }

    @Override
    public void inactive() {
        networkService.disconnect();
        addressService.onStop();
    }

    /**
     * View is requesting to pull address based on geolocation
     * this is done in an asynchronous way
     */
    public void requestAddressByGeolocation(){
        if( networkService.isConnected() ){
            addressService.geolocateAddress(new AddressResponse() {
                @Override
                public void onAddressResult(Address address) {
                    view.onAddressResult( address, networkService.isConnected() );
                }

                @Override
                public void onAddressError(Error error) {
                    view.onAddressError(error);
                }
            });
        }else{
            view.onAddressError( new Error("networkService has no connection"));
        }
    }

    /**
     * View is requesting addresses by query which is replied asynchronously
     */
    public void requestAddressSuggestions( String query ){
        if( networkService.isConnected() ){
            addressService.suggestAddress(query, new AddressesResponse() {
                @Override
                public void onAddressResults(List<Address> addresses) {
                    view.onAddressesSuggested( addresses );
                }

                @Override
                public void onAddressError(Error error) {
                    view.onAddressError( error );
                }
            });
        }
    }
}