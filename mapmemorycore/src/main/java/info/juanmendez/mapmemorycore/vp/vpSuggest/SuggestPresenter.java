package info.juanmendez.mapmemorycore.vp.vpSuggest;

import java.util.HashMap;
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

    public static final String SUGGEST_VIEW = "suggest_view";
    private HashMap<String,List<ShortAddress> > cachedSuggestedAddresses = new HashMap<>();

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
        refreshView();
    }

    private void refreshView() {

        view.displayAddress( selectedAddress.getAddress1() );

        if( cachedSuggestedAddresses.containsKey( selectedAddress.getAddress1() )){
            view.onAddressesSuggested( cachedSuggestedAddresses.get( selectedAddress.getAddress1()) );
        }else{
            requestAddressSuggestions( selectedAddress.getAddress1() );
        }
    }

    @Override
    public void inactive(Boolean rotated) {
        networkService.disconnect();
        addressService.onStop();

        if(!rotated){
            cachedSuggestedAddresses.clear();
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
                    cachedSuggestedAddresses.clear();
                    cachedSuggestedAddresses.put( selectedAddress.getAddress1(), results );
                    view.onAddressesSuggested( results );
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
}