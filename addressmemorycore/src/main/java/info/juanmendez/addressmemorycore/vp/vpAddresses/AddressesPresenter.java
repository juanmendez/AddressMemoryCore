package info.juanmendez.addressmemorycore.vp.vpAddresses;

import javax.inject.Inject;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.modules.MapModuleBase;
import info.juanmendez.addressmemorycore.utils.ModelUtils;
import info.juanmendez.addressmemorycore.vp.PresenterRotated;
import info.juanmendez.addressmemorycore.vp.vpAddress.AddressPresenter;
import io.realm.RealmResults;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class AddressesPresenter implements PresenterRotated<AddressesPresenter, AddressesView> {

    @Inject
    AddressProvider addressProvider;

    @Inject
    NavigationService navigationService;

    public static final String TAG = "addressesView";

    private RealmResults<ShortAddress> addresses;
    private AddressesView view;
    private Boolean rotated = false;

    public AddressesPresenter() {
        MapModuleBase.getInjector().inject(this);
    }

    @Override
    public AddressesPresenter getViewModel(AddressesView view) {
        this.view = view;
        view.injectAddresses( addressProvider.getAddresses() );

        return this;
    }

    @Override
    public void active(String action) {

    }

    @Override
    public void inactive(Boolean rotated){
        this.rotated = rotated;
    }

    public void selectAddress( ShortAddress address ){
        addressProvider.selectAddress( address );
    }

    public void addAddress(){
        addressProvider.selectAddress( new ShortAddress() );
        navigationService.request(AddressPresenter.ADDDRESS_EDIT_TAG);
    }

    public void updateAddress(ShortAddress shortAddress) {
        addressProvider.selectAddress(ModelUtils.cloneAddress(shortAddress));

        if( shortAddress.getAddressId() > 0 ){
            navigationService.request(AddressPresenter.ADDRESS_VIEW_TAG);
        }else{
            navigationService.request(AddressPresenter.ADDDRESS_EDIT_TAG);
        }

    }

    @Override
    public Boolean getRotated() {
        return rotated;
    }
}
