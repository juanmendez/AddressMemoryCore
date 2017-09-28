package info.juanmendez.mapmemorycore.addressmemorycore.vp.vpAddresses;

import android.app.Activity;

import java.util.List;

import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.vp.vpAddresses.AddressesView;
import info.juanmendez.addressmemorycore.vp.vpAddresses.AddressesPresenter;

import static org.powermock.api.mockito.PowerMockito.mock;


/**
 * Created by Juan Mendez on 6/26/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestAddressesFragment implements AddressesView {

    AddressesPresenter presenter;
    List<ShortAddress> addresses;

    public TestAddressesFragment() {
        presenter = new AddressesPresenter();
        presenter.getViewModel(this);
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
    public void active(String action) {
        presenter.active(action);
    }

    @Override
    public void inactive(Boolean rotated) {
        presenter.inactive(rotated);
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getTag() {
        return null;
    }

    @Override
    public Activity getActivity() {
        return mock(Activity.class);
    }
}
