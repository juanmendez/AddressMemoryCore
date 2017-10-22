package info.juanmendez.addressmemorycore.vp.vpAddress;

import android.content.Intent;
import android.databinding.Observable;

import javax.inject.Inject;

import info.juanmendez.addressmemorycore.BR;
import info.juanmendez.addressmemorycore.R;
import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.AddressService;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.PhotoService;
import info.juanmendez.addressmemorycore.dependencies.QuickResponse;
import info.juanmendez.addressmemorycore.dependencies.Response;
import info.juanmendez.addressmemorycore.dependencies.WidgetService;
import info.juanmendez.addressmemorycore.models.Commute;
import info.juanmendez.addressmemorycore.models.MapMemoryException;
import info.juanmendez.addressmemorycore.models.RouteMessage;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.models.SubmitError;
import info.juanmendez.addressmemorycore.modules.MapModuleBase;
import info.juanmendez.addressmemorycore.utils.ModelUtils;
import info.juanmendez.addressmemorycore.vp.Presenter;
import info.juanmendez.addressmemorycore.vp.vpSuggest.SuggestPresenter;

/**
 * Created by Juan Mendez on 6/26/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class AddressPresenter extends Observable.OnPropertyChangedCallback
                                implements Presenter<AddressViewModel,AddressView> {

    public static final String ADDRESS_JUST_CREATED = AddressPresenter.class.getName() + "/" + "ADDRESS_JUST_CREATED";
    private static final String ADDRESS_JUST_UPDATED = AddressPresenter.class.getName() + "/" + "ADDRESS_JUST_UPDATED";

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

    @Inject
    PhotoService photoService;

    private AddressView view;
    private AddressViewModel viewModel;
    private boolean rotated;

    public static final String ADDRESS_VIEW_TAG = "viewAddressTag";
    public static final String ADDDRESS_EDIT_TAG = "editAddressTag";

    /**
     * This property remind us of the last geoResult, and in case that is never requested then
     * the address values are empty strings. This result is used to figure out if the user is
     * editing any of the address fields.
     */
    private ShortAddress geoResult = new ShortAddress();

    @Override
    public AddressViewModel getViewModel(AddressView view) {
        this.view = view;
        MapModuleBase.getInjector().inject(this);

        return viewModel = new AddressViewModel();
    }

    @Override
    public void active( String params ) {


        if( !rotated ){
            viewModel.setAddress( addressProvider.getSelectedAddress() );

            if( params.equals(AddressPresenter.ADDRESS_JUST_CREATED)){
                view.doToast( view.getString(R.string.toast_address_created));
            }else if( params.equals( AddressPresenter.ADDRESS_JUST_UPDATED)){
                view.doToast( view.getString(R.string.toast_address_updated));
            }
        }

        networkService.reset();
        networkService.connect(result -> viewModel.isOnline.set(result));
        addressService.onStart(view.getActivity(), result -> viewModel.isGeoOn.set(result));
        viewModel.addOnPropertyChangedCallback(this);
    }

    @Override
    public void inactive(Boolean rotated) {
        this.rotated = rotated;
        networkService.disconnect();
        addressService.onStop();

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

    public void saveAddress(Response<RouteMessage> response) {

        if( isAddressValid() ){

            boolean isNew = viewModel.getAddress().getAddressId() == 0;

            addressProvider.updateAddressAsync(viewModel.getAddress(), new Response<ShortAddress>() {
                @Override
                public void onResult(ShortAddress result) {
                    addressProvider.selectAddress( result );
                    widgetService.updateList();
                    viewModel.setAddress(result);
                    response.onResult(new RouteMessage(ADDRESS_VIEW_TAG, isNew?ADDRESS_JUST_CREATED:ADDRESS_JUST_UPDATED ));
                }

                @Override
                public void onError(Exception exception) {
                    response.onError( new MapMemoryException( view.getString(R.string.address_save_error) ));
                }
            });

        }else{
            response.onError( MapMemoryException.build("On Submit there are errors").setErrors( addressProvider.validate(viewModel.getAddress()) ) );
        }
    }

    public void deleteAddress( Response<String> response ){

        ShortAddress addressToDelete = viewModel.getAddress();
        long addressId = addressToDelete.getAddressId();
        addressProvider.deleteAddressAsync(addressId, new Response<Boolean>() {

            @Override
            public void onResult(Boolean success) {
                if( success && SubmitError.emptyOrNull(addressToDelete.getPhotoLocation()) ){
                    photoService.deletePhoto( addressToDelete.getPhotoLocation() );
                }

                geoResult.setAddress1("");
                geoResult.setAddress2("");
                addressProvider.selectAddress( new ShortAddress());
                viewModel.setAddress( addressProvider.getSelectedAddress() );
                response.onResult( view.getString( R.string.toast_address_deleted) );
            }

            @Override
            public void onError(Exception exception) {
                response.onError( new MapMemoryException(view.getString( R.string.delete_error )));
            }
        });
        widgetService.updateList();
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
                    geoResult = result;
                    viewModel.getAddress().setMapId( result.getMapId() );
                    viewModel.setAddress1( result.getAddress1() );
                    viewModel.setAddress2( result.getAddress2() );
                    viewModel.notifyAddress();
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

    public void createNavigationIntent(QuickResponse<Intent> response){

        ShortAddress address = viewModel.getAddress();

        if( address.getCommute().getType().equals(Commute.UNDECIDED))
            return;

        address.setTimesVisited( address.getTimesVisited()+1);

        addressProvider.updateAddressAsync(address, new Response<ShortAddress>() {
            @Override
            public void onError(Exception exception) {

            }

            @Override
            public void onResult(ShortAddress result) {
                response.onResult( ModelUtils.fromAddress( viewModel.getAddress() ) );
            }
        });
    }

    /**
     * if presenter gets poked about an element being focused, then it
     * checks if there is connection and open up addressSuggestFragment
     */
    public void requestAddressSuggestion(){
        if( addressService.isConnected() && networkService.isConnected() ){
            navigationService.request(SuggestPresenter.SUGGEST_VIEW );
        }
    }

    @Override
    public void onPropertyChanged(Observable observable, int brId) {

        if( BR.address1==brId && !viewModel.getAddress1().equals(geoResult.getAddress1()) ){
            requestAddressSuggestion();
        }
        else
        if( BR.address2==brId && !viewModel.getAddress2().equals(geoResult.getAddress2()) ){
            requestAddressSuggestion();
        }

        if( AddressViewModel.addressEdits.indexOf(brId) >= 0 ){
            checkCanSubmit();
            checkCanDelete();
        }
        else
        if( AddressViewModel.commuteEdits.indexOf(brId) >= 0 && viewModel.getAddress().getAddressId() > 0 ){
            addressProvider.updateAddress(viewModel.getAddress());
        }
    }
}