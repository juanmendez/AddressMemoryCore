package info.juanmendez.addressmemorycore.vp.vpAddresses;

import android.databinding.Observable;

import javax.inject.Inject;

import info.juanmendez.addressmemorycore.BR;
import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.modules.MapModuleBase;
import info.juanmendez.addressmemorycore.utils.ModelUtils;
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

    public static final String TAG = "addressesView";

    private Boolean rotated = false;
    private AddressesViewModel viewModel;

    public AddressesPresenter() {
        MapModuleBase.getInjector().inject(this);
        viewModel = new AddressesViewModel();
    }

    @Override
    public void onPropertyChanged(Observable observable, int brId) {

        if(BR.selectedAddress == brId ){
            ShortAddress selectedAddress = viewModel.getSelectedAddress();

            if( selectedAddress != null ){
                addressProvider.selectAddress( ModelUtils.cloneAddress(viewModel.getSelectedAddress()) );

                if( selectedAddress.getAddressId() > 0 ){
                    navigationService.request(AddressPresenter.ADDRESS_VIEW_TAG);
                }else{
                    navigationService.request(AddressPresenter.ADDDRESS_EDIT_TAG);
                }
            }
        }
    }

    @Override
    public AddressesViewModel getViewModel(AddressesView view) {
        return viewModel;
    }

    @Override
    public void active(String action) {
        if( !rotated ){
            viewModel.setStreamingAddresses( addressProvider.getAddresses() );
            viewModel.setSelectedAddress(addressProvider.getSelectedAddress());
            viewModel.addOnPropertyChangedCallback( this );
        }
    }

    @Override
    public void inactive(Boolean rotated){
        this.rotated = rotated;
        viewModel.removeOnPropertyChangedCallback( this );
    }



    @Override
    public Boolean getRotated() {
        return rotated;
    }

    public void requestNewAddress() {
        addressProvider.selectAddress( new ShortAddress() );
        navigationService.request(AddressPresenter.ADDDRESS_EDIT_TAG);
    }
}
