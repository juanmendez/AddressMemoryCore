package info.juanmendez.mapmemorycore.vp.vpAddress;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.AddressService;
import info.juanmendez.mapmemorycore.dependencies.NavigationService;
import info.juanmendez.mapmemorycore.dependencies.NetworkService;
import info.juanmendez.mapmemorycore.dependencies.Response;
import info.juanmendez.mapmemorycore.models.MapMemoryException;
import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.models.SubmitError;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.utils.RxUtils;
import info.juanmendez.mapmemorycore.vp.Presenter;
import info.juanmendez.mapmemorycore.vp.vpSuggest.SuggestPresenter;
import rx.Subscription;

/**
 * Created by Juan Mendez on 6/26/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class AddressPresenter implements Presenter<AddressPresenter,AddressView> {

    @Inject
    AddressProvider addressProvider;

    @Inject
    AddressService addressService;

    @Inject
    NetworkService networkService;

    @Inject
    NavigationService navigationService;

    private AddressView view;
    private ShortAddress selectedAddress;

    public static final String ADDRESS_VIEW_TAG = "viewAddressTag";
    public static final String ADDDRESS_EDIT_TAG = "editAddressTag";
    private Subscription fileSubscription;

    @Override
    public AddressPresenter register(AddressView view) {
        this.view = view;
        MapCoreModule.getComponent().inject(this);
        return this;
    }

    @Override
    public void active( String action ) {

        selectedAddress = addressProvider.getSelectedAddress();

        networkService.reset();
        networkService.connect(new Response<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                view.onNetworkStatus( result );
            }

            @Override
            public void onError(Exception exception) {

            }
        });

        addressService.onStart( view.getActivity() );
        refreshView();
    }

    @Override
    public void inactive(Boolean rotated) {
        networkService.disconnect();
        addressService.onStop();

        RxUtils.unsubscribe(fileSubscription);
    }

    /**
     * this method was left public in case view wants to refresh itself.
     * presenter notifies view with latest values.
     */
    public void refreshView() {

        if( selectedAddress != null ){

            view.onPhotoSelected( new File( selectedAddress.getPhotoLocation()));
            view.onAddressResult( selectedAddress );
        }

        checkCanUpdate();
        checkCanDelete();
    }

    private void checkCanUpdate(){

        if( selectedAddress != null ) {
            view.canSubmit( addressProvider.validate(selectedAddress).isEmpty() );
        }
    }

    private void checkCanDelete(){

        if( selectedAddress != null ) {
            view.canDelete( SubmitError.initialized(selectedAddress.getAddressId()) );
        }
    }

    public void saveAddress(Response<ShortAddress> response) {

        List<SubmitError> errors = addressProvider.validate( addressProvider.getSelectedAddress() );

        if( errors.isEmpty() ){

            addressProvider.updateAddressAsync(selectedAddress, new Response<ShortAddress>() {
                @Override
                public void onResult(ShortAddress result) {
                    addressProvider.selectAddress( result );
                    response.onResult( result );
                    checkCanDelete();
                    checkCanUpdate();
                }

                @Override
                public void onError(Exception exception) {
                    response.onError( exception );
                }
            });

        }else{
            response.onError( MapMemoryException.build("On Submit there are errors").setErrors( errors ) );
        }
    }

    public void deleteAddress( Response<Boolean> response ){

        long addressId = selectedAddress.getAddressId();
        addressProvider.deleteAddressAsync( addressId, response );
    }

    /**
     * View is requesting to pull address based on geolocation
     * this is done in an asynchronous way
     */
    public void requestAddressByGeolocation(){
        if( networkService.isConnected() ){
            addressService.geolocateAddress(new Response<ShortAddress>() {
                @Override
                public void onResult(ShortAddress result) {
                    ShortAddress selectedAddress = addressProvider.getSelectedAddress();
                    selectedAddress.setMapId( result.getMapId() );
                    selectedAddress.setAddress1( result.getAddress1() );
                    selectedAddress.setAddress2( result.getAddress2() );

                    view.onAddressResult( selectedAddress );
                    checkCanUpdate();
                }

                @Override
                public void onError(Exception exception) {
                    view.onAddressError(exception);
                }
            });
        }else{
            view.onAddressError( new MapMemoryException("networkService has no connection"));
        }
    }

    public void setAddressName(String name ){
        selectedAddress.setName( name );
        view.canSubmit( addressProvider.validate( selectedAddress ).isEmpty() );
    }

    public void setAddressLines(String addressLine1, String addressLine2 ){

        if( !networkService.isConnected() ){
            selectedAddress.setAddress1( addressLine1 );
            selectedAddress.setAddress2( addressLine2 );
            view.canSubmit( addressProvider.validate( selectedAddress ).isEmpty() );
        }
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
}