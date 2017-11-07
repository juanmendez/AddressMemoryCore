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
import info.juanmendez.addressmemorycore.utils.AddressUtils;
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
    public static final String NEW_ADDRESS_REQUEST = AddressPresenter.class.getName() + "/" + "NEW_ADDRESS_REQUEST";

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

    private AddressView mView;
    private AddressViewModel mViewModel;
    private boolean mRotated;

    public static final String ADDRESS_VIEW_TAG = "viewAddressTag";
    public static final String ADDDRESS_EDIT_TAG = "editAddressTag";

    /**
     * This property remind us of the last mGeoResult, and in case that is never requested then
     * the address values are empty strings. This result is used to figure out if the user is
     * editing any of the address fields.
     */
    private ShortAddress mGeoResult = new ShortAddress();

    @Override
    public AddressViewModel getViewModel(AddressView view) {
        mView = view;
        MapModuleBase.getInjector().inject(this);

        return mViewModel = new AddressViewModel();
    }

    @Override
    public void active( String params ) {


        if( !mRotated){

            if( params.equals(AddressPresenter.ADDRESS_JUST_CREATED)){
                mView.doToast( mView.getString(R.string.toast_address_created));
            }else if( params.equals( AddressPresenter.ADDRESS_JUST_UPDATED)){
                mView.doToast( mView.getString(R.string.toast_address_updated));
            }else if( params.equals( AddressPresenter.NEW_ADDRESS_REQUEST ) &&
                      addressProvider.getSelectedAddress().getAddressId() > 0 ){
                //if there is already an address being edited and is saved
                //then is ok to include a new one
                addressProvider.selectAddress( new ShortAddress() );
            }

            mViewModel.setAddress(  addressProvider.getSelectedAddress() );
        }

        networkService.reset();
        networkService.connect(result -> mViewModel.isOnline.set(result));
        addressService.onStart(mView.getActivity(), result -> mViewModel.isGeoOn.set(result));
        mViewModel.addOnPropertyChangedCallback(this);
    }

    @Override
    public void inactive(Boolean rotated) {
        mRotated = rotated;
        networkService.disconnect();
        addressService.onStop();

        mViewModel.removeOnPropertyChangedCallback(this);
    }

    private void checkCanSubmit(){
        mViewModel.canSubmit.set( isAddressValid() );
    }

    private void checkCanDelete(){
        mViewModel.canDelete.set( SubmitError.initialized(mViewModel.getAddress().getAddressId()) );
    }

    private boolean isAddressValid(){
        return addressProvider.validate(mViewModel.getAddress()).isEmpty();
    }

    public void saveAddress(Response<RouteMessage> response) {

        if( isAddressValid() ){

            boolean isNew = mViewModel.getAddress().getAddressId() == 0;

            addressProvider.updateAddressAsync(mViewModel.getAddress(), new Response<ShortAddress>() {
                @Override
                public void onResult(ShortAddress result) {
                    addressProvider.selectAddress( result );
                    widgetService.updateList();
                    mViewModel.setAddress(result);
                    response.onResult(new RouteMessage(ADDRESS_VIEW_TAG, isNew?ADDRESS_JUST_CREATED:ADDRESS_JUST_UPDATED ));
                }

                @Override
                public void onError(Exception exception) {
                    response.onError( new MapMemoryException( mView.getString(R.string.address_save_error) ));
                }
            });

        }else{
            response.onError( MapMemoryException.build("On Submit there are errors").setErrors( addressProvider.validate(mViewModel.getAddress()) ) );
        }
    }

    public void deleteAddress( Response<String> response ){

        ShortAddress addressToDelete = mViewModel.getAddress();
        long addressId = addressToDelete.getAddressId();
        addressProvider.deleteAddressAsync(addressId, new Response<Boolean>() {

            @Override
            public void onResult(Boolean success) {
                if( success && SubmitError.emptyOrNull(addressToDelete.getPhotoLocation()) ){
                    photoService.deletePhoto( addressToDelete.getPhotoLocation() );
                }

                mGeoResult.setAddress1("");
                mGeoResult.setAddress2("");
                addressProvider.selectAddress( new ShortAddress());
                mViewModel.setAddress( addressProvider.getSelectedAddress() );
                response.onResult( mView.getString( R.string.toast_address_deleted) );
            }

            @Override
            public void onError(Exception exception) {
                response.onError( new MapMemoryException(mView.getString( R.string.delete_error )));
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
                    mGeoResult = result;
                    mViewModel.getAddress().setMapId( result.getMapId() );
                    mViewModel.setAddress1( result.getAddress1() );
                    mViewModel.setAddress2( result.getAddress2() );
                    mViewModel.notifyAddress();
                }

                @Override
                public void onError(Exception exception) {
                    mViewModel.setAddressException( exception );
                }
            });
        }else{
            mViewModel.setAddressException( new MapMemoryException("networkService has no connection") );
        }
    }

    public void createNavigationIntent(QuickResponse<Intent> response){

        ShortAddress address = mViewModel.getAddress();

        if( address.getCommute().getType().equals(Commute.UNDECIDED))
            return;

        address.setTimesVisited( address.getTimesVisited()+1);

        addressProvider.updateAddressAsync(address, new Response<ShortAddress>() {
            @Override
            public void onError(Exception exception) {

            }

            @Override
            public void onResult(ShortAddress result) {
                response.onResult( AddressUtils.fromAddress( mViewModel.getAddress() ) );
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

        if( BR.address1==brId && !mViewModel.getAddress1().equals(mGeoResult.getAddress1()) ){
            requestAddressSuggestion();
        }
        else
        if( BR.address2==brId && !mViewModel.getAddress2().equals(mGeoResult.getAddress2()) ){
            requestAddressSuggestion();
        }

        if( AddressViewModel.addressEdits.indexOf(brId) >= 0 ){
            checkCanSubmit();
            checkCanDelete();
        }
        else
        if( AddressViewModel.commuteEdits.indexOf(brId) >= 0 && mViewModel.getAddress().getAddressId() > 0 ){
            addressProvider.updateAddress(mViewModel.getAddress());
        }
    }
}