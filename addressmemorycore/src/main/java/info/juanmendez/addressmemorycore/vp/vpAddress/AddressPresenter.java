package info.juanmendez.addressmemorycore.vp.vpAddress;

import android.content.Intent;
import android.databinding.Observable;

import javax.inject.Inject;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.AddressService;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.Response;
import info.juanmendez.addressmemorycore.dependencies.WidgetService;
import info.juanmendez.addressmemorycore.models.AddressViewModel;
import info.juanmendez.addressmemorycore.models.MapMemoryException;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.models.SubmitError;
import info.juanmendez.addressmemorycore.modules.MapModuleBase;
import info.juanmendez.addressmemorycore.utils.ModelUtils;
import info.juanmendez.addressmemorycore.utils.RxUtils;
import info.juanmendez.addressmemorycore.vp.Presenter;
import info.juanmendez.addressmemorycore.vp.vpSuggest.SuggestPresenter;

import info.juanmendez.addressmemorycore.BR;
import rx.Subscription;

/**
 * Created by Juan Mendez on 6/26/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class AddressPresenter extends Observable.OnPropertyChangedCallback
                                implements Presenter<AddressViewModel,AddressView> {

    @Inject
    AddressProvider addressProvider;

    @Inject
    AddressService addressService;

    @Inject
    NetworkService networkService;

    @Inject
    NavigationService navigationService;

    @Inject
    WidgetService widgetService;

    private AddressView view;
    private AddressViewModel viewModel;

    public static final String ADDRESS_VIEW_TAG = "viewAddressTag";
    public static final String ADDDRESS_EDIT_TAG = "editAddressTag";
    private Subscription fileSubscription;

    @Override
    public AddressViewModel getViewModel(AddressView view) {
        this.view = view;
        MapModuleBase.getInjector().inject(this);

        return viewModel = new AddressViewModel();
    }

    @Override
    public void active( String action ) {
        
        viewModel.setAddress( addressProvider.getSelectedAddress() );

        networkService.reset();

        //TODO if possible make it to ShortResponse
        networkService.connect(new Response<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                viewModel.isOnline.set(result);
            }

            @Override
            public void onError(Exception exception) {
                viewModel.setAddressException( exception );
            }
        });

        addressService.onStart(view.getActivity(), result -> viewModel.isGeoOn.set(result));

        viewModel.addOnPropertyChangedCallback(this);
    }

    @Override
    public void inactive(Boolean rotated) {
        networkService.disconnect();
        addressService.onStop();

        RxUtils.unsubscribe(fileSubscription);
        viewModel.removeOnPropertyChangedCallback(this);
    }

    private void checkCanSubmit(){
        viewModel.canSubmit.set( isAddressValid() );
    }

    private void checkCanDelete(){
        viewModel.canDelete.set( SubmitError.initialized(viewModel.getAddress().getAddressId()) );
    }

    private boolean isAddressValid(){
        return addressProvider.validate(viewModel.getAddress()).isEmpty();
    }

    public void saveAddress(Response<ShortAddress> response) {

        if( isAddressValid() ){

            addressProvider.updateAddressAsync(viewModel.getAddress(), new Response<ShortAddress>() {
                @Override
                public void onResult(ShortAddress result) {
                    viewModel.setAddress(result);
                    addressProvider.selectAddress( result );
                    widgetService.refreshAddressList();
                }

                @Override
                public void onError(Exception exception) {
                    response.onError( exception );
                }
            });

        }else{
            response.onError( MapMemoryException.build("On Submit there are errors").setErrors( addressProvider.validate(viewModel.getAddress()) ) );
        }
    }

    public void deleteAddress( Response<Boolean> response ){

        long addressId = viewModel.getAddress().getAddressId();
        addressProvider.deleteAddressAsync( addressId, response );
        widgetService.refreshAddressList();
    }

    /**
     * View is requesting to pull address based on geolocation
     * this is done in an asynchronous way
     */
    public void requestAddressByGeolocation(){
        if( addressService.isConnected() ){
            addressService.geolocateAddress(new Response<ShortAddress>() {
                @Override
                public void onResult(ShortAddress result) {

                    viewModel.getAddress().setMapId( result.getMapId() );
                    viewModel.setAddress1( result.getAddress1() );
                    viewModel.setAddress2( result.getAddress2() );
                }

                @Override
                public void onError(Exception exception) {
                    viewModel.setAddressException( exception );
                }
            });
        }else{
            viewModel.setAddressException( new MapMemoryException("networkService has no connection") );
        }
    }

    public void setAddressName(String name ){
        viewModel.getAddress().setName( name );
        viewModel.notifyAddress();
    }

    public void setAddressLines(String addressLine1, String addressLine2 ){

        if( !networkService.isConnected() ){
            viewModel.getAddress().setAddress1( addressLine1 );
            viewModel.getAddress().setAddress2( addressLine2 );
            viewModel.notifyAddress();
        }
    }

    public void openNavigationApp(){

        Intent mapIntent = ModelUtils.fromAddress( viewModel.getAddress(), 'b' );
        view.getActivity().startActivity( mapIntent );
    }

    /**
     * if presenter gets poked about an element being focused, then it
     * checks if there is connection and open up addressSuggestFragment
     */
    public void textFocused(){
        if( networkService.isConnected() ){
            navigationService.request(SuggestPresenter.SUGGEST_VIEW );
        }
    }

    @Override
    public void onPropertyChanged(Observable observable, int brId) {

        //TODO: refactor this condition
        if(BR.addressException != brId ){
            checkCanSubmit();
            checkCanDelete();
        }
    }
}