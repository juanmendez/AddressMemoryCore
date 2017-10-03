package info.juanmendez.addressmemorycore.vp.vpSuggest;

import android.databinding.Observable;

import java.util.List;

import javax.inject.Inject;

import info.juanmendez.addressmemorycore.BR;
import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.AddressService;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.Response;
import info.juanmendez.addressmemorycore.models.MapMemoryException;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.models.SuggestAddressViewModel;
import info.juanmendez.addressmemorycore.modules.MapModuleBase;
import info.juanmendez.addressmemorycore.vp.PresenterRotated;

/**
 * Created by Juan Mendez on 8/16/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class SuggestPresenter extends Observable.OnPropertyChangedCallback implements PresenterRotated<SuggestAddressViewModel,SuggestView> {

    @Inject
    AddressProvider addressProvider;

    @Inject
    AddressService addressService;

    @Inject
    NetworkService networkService;

    @Inject
    NavigationService navigationService;

    private SuggestView view;
    private SuggestAddressViewModel viewModel;
    private boolean rotated = false;

    public static final String SUGGEST_VIEW = "suggest_view";

    @Override
    public SuggestAddressViewModel getViewModel(SuggestView view) {
        this.view = view;
        MapModuleBase.getInjector().inject(this);
        viewModel = new SuggestAddressViewModel();
        viewModel.addOnPropertyChangedCallback(this);
        return viewModel;
    }

    @Override
    public void active(String action) {
        networkService.reset();

        //We update addressEdited in that way we generate matching addresses.
        //We don't do that if the device has only rotated.
        networkService.connect(result -> {
            if( !rotated ){
                viewModel.setSelectedAddress(addressProvider.getSelectedAddress());
            }
        });
    }

    @Override
    public void inactive(Boolean isRotated) {
        this.rotated = isRotated;
        networkService.disconnect();
        addressService.onStop();
        viewModel.removeOnPropertyChangedCallback(this);
    }

    /**
     * Through addressService we look for matching addresses and update the viewModel
     * so the matching address are reflected in the view.
     * TODO: make exception messages come from resource strings
     */
    private void searchForMatchingAddresses(){

        String query = viewModel.getAddressEdited();

        if( !addressService.isConnected() ) {
            viewModel.setAddressException(new MapMemoryException("There is no connection"));
        } else if( query.trim().isEmpty() ) {
            viewModel.clearMatchingResults();
        } else {
            addressService.suggestAddress(query, new Response<List<ShortAddress>>() {
                @Override
                public void onResult(List<ShortAddress> results ) {
                    viewModel.setMatchingAddresses(results);
                }

                @Override
                public void onError(Exception exception) {
                    viewModel.setAddressException(exception);
                }
            });
        }
    }

    /**
     * User has selected one address!
     */
    public void updateFromPickedAddress(){
        ShortAddress pickedAddress = viewModel.getPickedMatchingAddress();
        if( pickedAddress != null ){
            ShortAddress selectedAddress = addressProvider.getSelectedAddress();
            selectedAddress.setAddress1( pickedAddress.getAddress1() );
            selectedAddress.setAddress2( pickedAddress.getAddress2() );
            selectedAddress.setMapId( pickedAddress.getMapId() );
            selectedAddress.setLat( pickedAddress.getLat() );
            selectedAddress.setLon( pickedAddress.getLon() );
            navigationService.goBack();
        }
    }

    @Override
    public Boolean getRotated() {
        return rotated;
    }

    @Override
    public void onPropertyChanged(Observable observable, int brId) {
        if( brId == BR.addressEdited){
            searchForMatchingAddresses();
        }else if( brId == BR.pickedMatchingAddress ){
            updateFromPickedAddress();
        }
    }
}