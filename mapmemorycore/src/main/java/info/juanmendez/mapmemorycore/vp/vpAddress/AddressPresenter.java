package info.juanmendez.mapmemorycore.vp.vpAddress;
import javax.inject.Inject;

import info.juanmendez.mapmemorycore.dependencies.Navigation;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AutocompleteService;
import info.juanmendez.mapmemorycore.dependencies.db.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.network.NetworkService;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.vp.ViewPresenter;

/**
 * Created by Juan Mendez on 6/26/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class AddressPresenter implements ViewPresenter<AddressPresenter,AddressFragment>{

    @Inject
    AddressProvider addressProvider;

    @Inject
    AutocompleteService autocompleteService;

    @Inject
    NetworkService networkService;

    @Inject
    Navigation navigation;

    AddressFragment view;

    public static final String VIEW_TAG = "addressView";
    public static final String EDIT_TAG = "editAddressView";

    @Override
    public AddressPresenter register(AddressFragment view) {
        this.view = view;
        MapCoreModule.getComponent().inject(this);

        return this;
    }

    @Override
    public void active( String action ) {
        networkService.connect(this, available -> {
            view.showAddress( addressProvider.getSelectedAddress(), available );
        });
    }

    @Override
    public void inactive() {
        networkService.disconnect(this);
    }



}
