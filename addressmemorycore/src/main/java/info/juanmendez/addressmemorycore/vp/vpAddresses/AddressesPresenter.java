package info.juanmendez.addressmemorycore.vp.vpAddresses;

import android.databinding.Observable;

import javax.inject.Inject;

import info.juanmendez.addressmemorycore.BR;
import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.modules.MapModuleBase;
import info.juanmendez.addressmemorycore.utils.AddressUtils;
import info.juanmendez.addressmemorycore.vp.PresenterRotated;
import info.juanmendez.addressmemorycore.vp.vpAddress.AddressPresenter;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class AddressesPresenter extends Observable.OnPropertyChangedCallback implements PresenterRotated<AddressesViewModel, AddressesView> {

    @Inject
    AddressProvider addressProvider;

    @Inject
    NavigationService navigationService;

    public static final String ADDRESSES_TAG = "addressesView";
    private Boolean mRotated = false;
    private AddressesViewModel mViewModel;

    public AddressesPresenter() {
        MapModuleBase.getInjector().inject(this);
        mViewModel = new AddressesViewModel();
    }

    @Override
    public AddressesViewModel getViewModel(AddressesView view) {
        return mViewModel;
    }

    @Override
    public void active(String params) {

        mViewModel.setStreamingAddresses( addressProvider.getAddresses() );
        mViewModel.setSelectedAddress(addressProvider.getSelectedAddress());
        mViewModel.addOnPropertyChangedCallback( this );
    }

    @Override
    public void inactive(Boolean rotated){
        mRotated = rotated;
        mViewModel.removeOnPropertyChangedCallback( this );
    }

    @Override
    public Boolean getRotated() {
        return mRotated;
    }

    public void requestNewAddress() {
        addressProvider.selectAddress( new ShortAddress() );
        navigationService.request(AddressPresenter.ADDDRESS_EDIT_TAG);
    }

    @Override
    public void onPropertyChanged(Observable observable, int brId) {

        if(BR.selectedAddress == brId ){
            ShortAddress selectedAddress = mViewModel.getSelectedAddress();

            if( selectedAddress != null ){
                addressProvider.selectAddress( AddressUtils.cloneAddress(mViewModel.getSelectedAddress()) );

                if( selectedAddress.getAddressId() > 0 ){
                    navigationService.request(AddressPresenter.ADDRESS_VIEW_TAG);
                }else{
                    navigationService.request(AddressPresenter.ADDDRESS_EDIT_TAG);
                }
            }
        }
    }
}
