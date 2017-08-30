package info.juanmendez.mapmemorycore.vp.vpAddresses;

import javax.inject.Inject;

import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.NavigationService;
import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.utils.ModelUtils;
import info.juanmendez.mapmemorycore.vp.PresenterRotated;
import info.juanmendez.mapmemorycore.vp.vpAddress.AddressPresenter;
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
    private Boolean rotated;

    public AddressesPresenter() {
        MapCoreModule.getComponent().inject(this);
    }

    @Override
    public AddressesPresenter register(AddressesView view) {
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
        navigationService.request(AddressPresenter.ADDDRESS_EDIT_TAG);
    }

    @Override
    public Boolean getRotated() {
        return rotated;
    }
}
