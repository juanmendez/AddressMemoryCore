package info.juanmendez.mapmemorycore.vp.vpAddress;
import javax.inject.Inject;

import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.services.AutocompleteService;
import info.juanmendez.mapmemorycore.vp.ViewPresenter;

/**
 * Created by Juan Mendez on 6/26/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class AddressPresenter implements ViewPresenter<AddressPresenter,AddressView> {

    @Inject
    AddressProvider addressProvider;

    @Inject
    AutocompleteService autocompleteService;

    AddressView view;

    public static final String VIEW_TAG = "addressView";
    public static final String EDIT_TAG = "editAddressView";

    public AddressPresenter() {
        MapCoreModule.getComponent().inject(this);
    }

    @Override
    public AddressPresenter onStart(AddressView view) {
        this.view = view;

        if( addressProvider.getSelectedAddress() != null ){
            view.showAddress( addressProvider.getSelectedAddress() );
        }
        return this;
    }

    @Override
    public AddressPresenter onPause() {
        return this;
    }
}
