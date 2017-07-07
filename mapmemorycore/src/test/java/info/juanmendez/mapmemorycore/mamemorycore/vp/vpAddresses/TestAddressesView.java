package info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddresses;

import java.util.List;

import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.vp.vpAddresses.AddressesPresenter;
import info.juanmendez.mapmemorycore.vp.vpAddresses.AddressesView;


/**
 * Created by Juan Mendez on 6/26/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestAddressesView implements AddressesView {

    AddressesPresenter presenter;
    List<Address> addresses;

    public TestAddressesView() {
        presenter = new AddressesPresenter();
    }

    public void onStart(){
        presenter.onStart(this);
    }

    public void onPause(){
        presenter.onPause();
    }

    @Override
    public void injectAddresses(List<Address> addresses) {
        this.addresses = addresses;
        System.out.println( "refresh addresses, len: " + addresses.size() );
    }

    public AddressesPresenter getPresenter() {
        return presenter;
    }

    public List<Address> getAddresses() {
        return addresses;
    }
}
