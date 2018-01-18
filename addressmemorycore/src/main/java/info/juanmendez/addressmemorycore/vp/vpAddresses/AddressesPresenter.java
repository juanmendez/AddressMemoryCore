package info.juanmendez.addressmemorycore.vp.vpAddresses;

import android.databinding.Observable;

import info.juanmendez.addressmemorycore.BR;
import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.models.AppConfig;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.modules.AddressCoreModule;
import info.juanmendez.addressmemorycore.utils.AddressUtils;
import info.juanmendez.addressmemorycore.utils.ValueUtils;
import info.juanmendez.addressmemorycore.vp.PresenterRotated;
import info.juanmendez.addressmemorycore.vp.vpAddress.AddressPresenter;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class AddressesPresenter extends Observable.OnPropertyChangedCallback implements PresenterRotated<AddressesViewModel, AddressesView> {

    private AddressProvider mAddressProvider;
    private NavigationService mNavigationService;

    public static final String ADDRESSES_TAG = "addressesView";
    private boolean mLastRotated = false;
    private AddressesViewModel mViewModel;
    private AddressesView mAddressesView;
    private AppConfig mAppConfig;


    public AddressesPresenter( AddressCoreModule module ) {

        mAddressProvider = module.getAddressProvider();
        mNavigationService = module.getNavigationService();
        mAppConfig = module.getAppConfig();
        mViewModel = new AddressesViewModel();
    }

    @Override
    public AddressesViewModel getViewModel(AddressesView view) {
        mAddressesView = view;
        return mViewModel;
    }

    @Override
    public void active(String params) {

        mViewModel.setStreamingAddresses( mAddressProvider.getAddresses() );


        mViewModel.setSelectedAddress(mAddressProvider.getSelectedAddress());
        mViewModel.addOnPropertyChangedCallback( this );

        if(ValueUtils.isLong( params )){

            ShortAddress result = mAddressProvider.getAddress(Long.parseLong(params));

            if( result != null ){
                mViewModel.setSelectedAddress( result );
            }
        }
    }

    @Override
    public void inactive(Boolean rotated){
        mLastRotated = rotated;
        mViewModel.removeOnPropertyChangedCallback( this );
    }

    @Override
    public Boolean getRotated() {
        return mLastRotated;
    }

    public void requestNewAddress() {

        boolean allowNewAddress = false;

        if( mAppConfig.getFlavor().equals( AppConfig.LITE ) ){

            allowNewAddress = mAddressProvider.countAddresses() < mAppConfig.getAddressLimit();

            if( !allowNewAddress ){
                mAddressesView.notifyAddressLimit();
            }

        }else if( mAppConfig.getFlavor().equals( AppConfig.PRO )){
            allowNewAddress = true;
        }

        if( allowNewAddress ){
            ShortAddress newAddress = new ShortAddress();
            mAddressProvider.selectAddress( newAddress );
            mViewModel.setSelectedAddress( newAddress );
            mNavigationService.request(AddressPresenter.ADDDRESS_EDIT_TAG);
        }
    }

    @Override
    public void onPropertyChanged(Observable observable, int brId) {

        if(BR.selectedAddress == brId ){
            ShortAddress selectedAddress = mViewModel.getSelectedAddress();

            if( selectedAddress != null ){
                mAddressProvider.selectAddress( AddressUtils.cloneAddress(mViewModel.getSelectedAddress()) );

                if( selectedAddress.getAddressId() > 0 ){
                    mNavigationService.request(AddressPresenter.ADDRESS_VIEW_TAG, Long.toString(selectedAddress.getAddressId()) );
                }else{
                    mNavigationService.request(AddressPresenter.ADDDRESS_EDIT_TAG + "/" + "new");
                }
            }
        }
    }
}
