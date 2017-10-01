package info.juanmendez.addressmemorycore.vp.vpSuggest;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.AddressService;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.Response;
import info.juanmendez.addressmemorycore.models.MapMemoryException;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.models.SubmitError;
import info.juanmendez.addressmemorycore.modules.MapModuleBase;
import info.juanmendez.addressmemorycore.vp.PresenterRotated;

/**
 * Created by Juan Mendez on 8/16/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class SuggestPresenter  implements PresenterRotated<SuggestPresenter,SuggestView> {

    @Inject
    AddressProvider addressProvider;

    @Inject
    AddressService addressService;

    @Inject
    NetworkService networkService;

    @Inject
    NavigationService navigationService;

    private ShortAddress selectedAddress;

    private SuggestView view;
    private boolean rotated = false;

    public static final String SUGGEST_VIEW = "suggest_view";

    @Override
    public SuggestPresenter getViewModel(SuggestView view) {
        this.view = view;
        MapModuleBase.getInjector().inject(this);
        return this;
    }

    @Override
    public void active(String action) {

        selectedAddress = addressProvider.getSelectedAddress();
        networkService.reset();
        networkService.connect(new Response<Boolean>() {
            @Override
            public void onResult(Boolean result) {

                if( !rotated && !SubmitError.emptyOrNull(selectedAddress.getAddress1()))
                    requestAddressSuggestions( selectedAddress.getAddress1() );
            }

            @Override
            public void onError(Exception exception) {
            }
        });

        addressService.onStart(view.getActivity(), result -> {

        });

        if( view.getPrintedAddress() != null && view.getPrintedAddress().isEmpty() ){
            view.setPrintedAddress( selectedAddress.getAddress1() );
        }
    }

    @Override
    public void inactive(Boolean rotated) {
        networkService.disconnect();
        addressService.onStop();
        this.rotated = rotated;
    }

    /**
     * View is requesting addresses by query which is replied asynchronously
     */
    public void requestAddressSuggestions( @NonNull String query ){
        selectedAddress.setAddress1( query );
        if( addressService.isConnected() && !query.isEmpty() ){
            addressService.suggestAddress(query, new Response<List<ShortAddress>>() {
                @Override
                public void onResult(List<ShortAddress> results ) {
                    view.setSuggestedAddresses( results );
                }

                @Override
                public void onError(Exception exception) {
                    view.onError( exception );
                }
            });
        }else if( !addressService.isConnected() ) {
            view.onError(new MapMemoryException("There is no connection"));
        }
        else if( query.isEmpty() ) {
            view.onError(new MapMemoryException("query is empty"));
        }
    }


    /**
     * View is providing a selected address
     */
    public void setAddressSelected( @NonNull  ShortAddress address ){
        selectedAddress.setAddress1( address.getAddress1() );
        selectedAddress.setAddress2( address.getAddress2() );
        selectedAddress.setMapId( address.getMapId() );
        selectedAddress.setLat( address.getLat() );
        selectedAddress.setLon( address.getLon() );
        navigationService.goBack();
    }

    @Override
    public Boolean getRotated() {
        return rotated;
    }
}