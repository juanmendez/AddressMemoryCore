package info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddresses;

import java.util.List;

import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.vp.vpAddresses.AddressesPresenter;
import info.juanmendez.mapmemorycore.vp.vpAddresses.AddressesFragment;


/**
 * Created by Juan Mendez on 6/26/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestAddressesFragment implements AddressesFragment {

    AddressesPresenter presenter;
    List<Address> addresses;

    public TestAddressesFragment() {
        presenter = new AddressesPresenter();
        presenter.register(this);
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

    @Override
    public void setActive(Boolean active, String action) {
        if( active ){
            presenter.active(action);
        }else{
            presenter.inactive();
        }
    }
}
