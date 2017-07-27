package info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddresses;

import android.app.Activity;

import java.util.List;

import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.vp.vpAddresses.AddressesFragment;
import info.juanmendez.mapmemorycore.vp.vpAddresses.AddressesPresenter;

import static org.powermock.api.mockito.PowerMockito.mock;


/**
 * Created by Juan Mendez on 6/26/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestAddressesFragment implements AddressesFragment {

    AddressesPresenter presenter;
    List<ShortAddress> addresses;

    public TestAddressesFragment() {
        presenter = new AddressesPresenter();
        presenter.register(this);
    }

    @Override
    public void injectAddresses(List<ShortAddress> addresses) {
        this.addresses = addresses;
        System.out.println( "refresh addresses, len: " + addresses.size() );
    }

    public AddressesPresenter getPresenter() {
        return presenter;
    }

    public List<ShortAddress> getAddresses() {
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

    @Override
    public Activity getActivity() {
        return mock(Activity.class);
    }
}
