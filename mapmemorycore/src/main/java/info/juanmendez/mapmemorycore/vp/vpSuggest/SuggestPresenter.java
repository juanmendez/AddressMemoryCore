package info.juanmendez.mapmemorycore.vp.vpSuggest;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.AddressService;
import info.juanmendez.mapmemorycore.dependencies.NavigationService;
import info.juanmendez.mapmemorycore.dependencies.NetworkService;
import info.juanmendez.mapmemorycore.dependencies.Response;
import info.juanmendez.mapmemorycore.models.MapMemoryException;
import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.vp.ViewPresenter;

/**
 * Created by Juan Mendez on 8/16/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class SuggestPresenter  implements ViewPresenter<SuggestPresenter,SuggestView> {

    @Inject
    AddressProvider addressProvider;

    @Inject
    AddressService addressService;

    @Inject
    NetworkService networkService;

    @Inject
    NavigationService navigationService;

    ShortAddress selectedAddress;

    private SuggestView view;
    private boolean rotated;

    public static final String SUGGEST_VIEW = "suggest_view";

    @Override
    public SuggestPresenter register(SuggestView view) {
        this.view = view;

        MapCoreModule.getComponent().inject(this);
        return this;
    }

    @Override
    public void active(String action) {

        selectedAddress = addressProvider.getSelectedAddress();
        networkService.reset();
        networkService.connect(new Response<Boolean>() {
            @Override
            public void onResult(Boolean result) {
            }

            @Override
            public void onError(Exception exception) {
            }
        });

        addressService.onStart( view.getActivity() );

        view.setPrintedAddress( selectedAddress.getAddress1() );
    }

    @Override
    public void inactive(Boolean rotated) {
        networkService.disconnect();
        addressService.onStop();
        this.rotated = rotated;

        if( !rotated ){
            view.setPrintedAddress( "" );
        }
    }

    /**
     * View is requesting addresses by query which is replied asynchronously
     */
    public void requestAddressSuggestions( String query ){
        selectedAddress.setAddress1( query );
        if( networkService.isConnected() && !query.isEmpty() ){
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
        }else if( !networkService.isConnected() ) {
            view.onError(new MapMemoryException("networkService has no connection"));
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
        //navigationService.goBack();
    }
}