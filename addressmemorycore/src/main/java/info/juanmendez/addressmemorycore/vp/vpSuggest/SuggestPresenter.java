package info.juanmendez.addressmemorycore.vp.vpSuggest;

import android.databinding.Observable;

import java.util.List;

import info.juanmendez.addressmemorycore.BR;
import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.AddressService;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.Response;
import info.juanmendez.addressmemorycore.models.AddressException;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.modules.AddressCoreModule;
import info.juanmendez.addressmemorycore.vp.PresenterRotated;

/**
 * Created by Juan Mendez on 8/16/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class SuggestPresenter extends Observable.OnPropertyChangedCallback implements PresenterRotated<SuggestViewModel,SuggestView> {


    private AddressProvider mAddressProvider;
    private AddressService mAddressService;
    private NetworkService mNetworkService;
    private NavigationService mNavigationService;

    private SuggestView mView;
    private SuggestViewModel mViewModel;
    private boolean mLastRotated;
    public static final String SUGGEST_VIEW = "suggest_view";

    public SuggestPresenter(AddressCoreModule module) {
        mAddressProvider = module.getAddressProvider();
        mAddressService = module.getAddressService();
        mNetworkService = module.getNetworkService();
        mNavigationService = module.getNavigationService();
    }

    @Override
    public SuggestViewModel getViewModel(SuggestView view) {
        mView = view;
        mViewModel = new SuggestViewModel();
        return mViewModel;
    }

    @Override
    public void active(String params ) {

        if( !mLastRotated){
            mViewModel.setSelectedAddress(mAddressProvider.getSelectedAddress());
        }

        mViewModel.addOnPropertyChangedCallback(this);

        //We update addressEdited in that way we generate matching addresses.
        mNetworkService.connect(result -> {});

        mAddressService.onStart(mView.getActivity(), result -> {
            searchForMatchingAddresses();
        });
    }

    public String getAddressEdited(){
        return mAddressProvider.getSelectedAddress().getAddress1();
    }

    @Override
    public void inactive(Boolean isRotated) {
        mLastRotated = isRotated;
        mNetworkService.disconnect();
        mAddressService.onStop();
        mViewModel.removeOnPropertyChangedCallback(this);
    }

    /**
     * Through mAddressService we look for matching addresses and update the mViewModel
     * so the matching address are reflected in the mView.
     * TODO: make exception messages come from resource strings
     */
    private void searchForMatchingAddresses(){

        String query = mViewModel.getAddressEdited();

        if( !mAddressService.isConnected() ) {
            mViewModel.setAddressException(new AddressException("There is no connection"));
        } else if( query.trim().isEmpty() ) {
            mViewModel.clearMatchingResults();
        } else {
            mAddressService.suggestAddress(query, new Response<List<ShortAddress>>() {
                @Override
                public void onResult(List<ShortAddress> results ) {
                    mViewModel.setMatchingAddresses(results);
                }

                @Override
                public void onError(Exception exception) {
                    mViewModel.setAddressException(exception);
                }
            });
        }
    }

    /**
     * User has selected one address!
     */
    public void updateFromPickedAddress(){
        ShortAddress pickedAddress = mViewModel.getPickedAddress();
        if( pickedAddress != null ){
            ShortAddress selectedAddress = mAddressProvider.getSelectedAddress();
            selectedAddress.setAddress1( pickedAddress.getAddress1() );
            selectedAddress.setAddress2( pickedAddress.getAddress2() );
            selectedAddress.setMapId( pickedAddress.getMapId() );
            selectedAddress.setLat( pickedAddress.getLat() );
            selectedAddress.setLon( pickedAddress.getLon() );
            mNavigationService.goBack();
        }
    }

    @Override
    public Boolean getRotated() {
        return mLastRotated;
    }

    @Override
    public void onPropertyChanged(Observable observable, int brId) {
        if( brId == BR.addressEdited){
            searchForMatchingAddresses();
        }else if( brId == BR.pickedAddress ){
            updateFromPickedAddress();
        }
    }
}