package info.juanmendez.addressmemorycore.vp.vpAddress;

import android.content.Intent;
import android.databinding.Observable;

import java.util.List;

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
import info.juanmendez.addressmemorycore.models.AddressException;
import info.juanmendez.addressmemorycore.models.RouteMessage;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.models.SubmitError;
import info.juanmendez.addressmemorycore.modules.AddressCoreModule;
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

    private AddressProvider mAddressProvider;
    private AddressService mAddressService;
    private NetworkService mNetworkService;
    private NavigationService mNavigationService;
    private WidgetService mWidgetService;
    private PhotoService mPhotoService;

    private AddressView mView;
    private AddressViewModel mViewModel;
    private boolean mLastRotated;

    public static final String ADDRESS_VIEW_TAG = "viewAddressTag";
    public static final String ADDDRESS_EDIT_TAG = "editAddressTag";

    /**
     * This property remind us of the last mGeoResult, and in case that is never requested then
     * the address values are empty strings. This result is used to figure out if the user is
     * editing any of the address fields.
     */
    private ShortAddress mGeoResult = new ShortAddress();

    public AddressPresenter(AddressCoreModule module) {

        mAddressProvider = module.getAddressProvider();
        mAddressService = module.getAddressService();
        mNetworkService = module.getNetworkService();
        mNavigationService = module.getNavigationService();
        mWidgetService = module.getWidgetService();
        mPhotoService = module.getPhotoService();
    }

    @Override
    public AddressViewModel getViewModel(AddressView view) {
        mView = view;
        return mViewModel = new AddressViewModel();
    }

    @Override
    public void active( String params ) {

        if( !mLastRotated){

            if( params.equals(AddressPresenter.ADDRESS_JUST_CREATED)){
                mView.iToast( mView.getString(R.string.toast_address_created));
            }else if( params.equals( AddressPresenter.ADDRESS_JUST_UPDATED)){
                mView.iToast( mView.getString(R.string.toast_address_updated));
            }else if( params.equals( AddressPresenter.NEW_ADDRESS_REQUEST ) &&
                      mAddressProvider.getSelectedAddress().getAddressId() > 0 ){
                //if there is already an address being edited and is saved
                //then is ok to include a new one
                mAddressProvider.selectAddress( new ShortAddress() );
            }

            mViewModel.setAddress(  mAddressProvider.getSelectedAddress() );
        }

        mNetworkService.connect(result ->{
            mViewModel.isOnline.set(result);
            mViewModel.notifyPropertyChanged( BR.isOnline );
        } );

        mAddressService.onStart(mView.getActivity(), result -> mViewModel.isGeoOn.set(result));
        mViewModel.addOnPropertyChangedCallback(this);
    }

    @Override
    public void inactive(Boolean rotated) {
        mLastRotated = rotated;
        mNetworkService.disconnect();
        mAddressService.onStop();

        mViewModel.removeOnPropertyChangedCallback(this);
    }

    /**
     * Aside from returning if the form values are valid,
     * this method also feeds mViewModel.submitErrors.
     */
    private void checkCanSubmit(){
        List<SubmitError> submitErrors = getInvalidFields();
        mViewModel.submitErrors.clear();
        mViewModel.submitErrors.addAll( submitErrors );
        mViewModel.canSubmit.set( submitErrors.isEmpty() );
    }

    /**
     * Delete is only available for stored addresses
     */
    private void checkCanDelete(){
        mViewModel.canDelete.set( SubmitError.initialized(mViewModel.getAddress().getAddressId()) );
    }

    private List<SubmitError> getInvalidFields(){
        return mAddressProvider.validate(mViewModel.getAddress());
    }

    public void saveAddress(Response<RouteMessage> response) {

        if( getInvalidFields().isEmpty() ){

            boolean isNew = mViewModel.getAddress().getAddressId() == 0;

            mAddressProvider.updateAddressAsync(mViewModel.getAddress(), new Response<ShortAddress>() {
                @Override
                public void onResult(ShortAddress result) {
                    mAddressProvider.selectAddress( result );
                    mWidgetService.updateList();
                    mViewModel.setAddress(result);
                    response.onResult(new RouteMessage(ADDRESS_VIEW_TAG, Long.toString(result.getAddressId()) ));
                }

                @Override
                public void onError(Exception exception) {
                    response.onError( new AddressException( mView.getString(R.string.address_save_error) ));
                }
            });

        }else{
            response.onError( AddressException.build("On Submit there are errors").setErrors( mAddressProvider.validate(mViewModel.getAddress()) ) );
        }
    }

    public void deleteAddress( Response<String> response ){

        ShortAddress addressToDelete = mViewModel.getAddress();
        long addressId = addressToDelete.getAddressId();
        mAddressProvider.deleteAddressAsync(addressId, new Response<Boolean>() {

            @Override
            public void onResult(Boolean success) {
                if( success && SubmitError.emptyOrNull(addressToDelete.getPhotoLocation()) ){
                    mPhotoService.deletePhoto( addressToDelete.getPhotoLocation() );
                }

                mGeoResult.setAddress1("");
                mGeoResult.setAddress2("");
                mAddressProvider.selectAddress( new ShortAddress());
                mViewModel.setAddress( mAddressProvider.getSelectedAddress() );
                response.onResult( mView.getString( R.string.toast_address_deleted) );
            }

            @Override
            public void onError(Exception exception) {
                response.onError( new AddressException(mView.getString( R.string.delete_error )));
            }
        });
        mWidgetService.updateList();
    }

    /**
     * View is requesting to pull address based on geolocation
     * this is done in an asynchronous way
     */
    public void requestAddressByGeolocation(){
        if( mAddressService.isConnected() ){
            mAddressService.geolocateAddress(new Response<ShortAddress>() {
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
            mViewModel.setAddressException( new AddressException("mNetworkService has no connection") );
        }
    }

    public void createNavigationIntent(QuickResponse<Intent> response){

        ShortAddress address = mViewModel.getAddress();

        if( address.getCommute().getType().equals(Commute.UNDECIDED))
            return;

        address.setTimesVisited( address.getTimesVisited()+1);

        mAddressProvider.updateAddressAsync(address, new Response<ShortAddress>() {
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
        if( mAddressService.isConnected() && mNetworkService.isConnected() ){
            mNavigationService.request(SuggestPresenter.SUGGEST_VIEW );
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
        else if( AddressViewModel.commuteEdits.indexOf(brId) >= 0 && mViewModel.getAddress().getAddressId() > 0 ){
            mAddressProvider.updateAddress(mViewModel.getAddress());
        }
    }
}