package info.juanmendez.mapmemorycore.vp.vpAddress;
import info.juanmendez.mapmemorycore.vp.ViewPresenter;

/**
 * Created by Juan Mendez on 6/26/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class AddressPresenter implements ViewPresenter<AddressPresenter,AddressView> {
    AddressView view;

    @Override
    public AddressPresenter onStart(AddressView view) {
        this.view = view;

        return this;
    }

    @Override
    public AddressPresenter onPause() {

        return this;
    }
}
