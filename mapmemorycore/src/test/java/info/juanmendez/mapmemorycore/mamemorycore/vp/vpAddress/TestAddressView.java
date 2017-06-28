package info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddress;

import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.vp.vpAddress.AddressPresenter;
import info.juanmendez.mapmemorycore.vp.vpAddress.AddressView;


/**
 * Created by Juan Mendez on 6/28/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestAddressView implements AddressView {
    AddressPresenter presenter;

    public TestAddressView() {
        presenter = new AddressPresenter();
    }

    public void onStart(){
        presenter.onStart(this);
    }

    public void onPause(){
        presenter.onPause();
    }

    public AddressPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showAddress(Address address) {
        System.out.println( "address: " + address.getName() + " populates in here" );
    }
}
