package info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddress;

import android.app.Activity;

import java.util.List;

import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.vp.vpAddress.AddressFragment;
import info.juanmendez.mapmemorycore.vp.vpAddress.AddressPresenter;

import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by Juan Mendez on 6/28/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class TestAddressFragment implements AddressFragment {
    AddressPresenter presenter;

    public TestAddressFragment() {
        presenter = new AddressPresenter();
        presenter.register(this);
    }

    public AddressPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onAddressResult(Address address, Boolean online) {

    }

    @Override
    public void onAddressError(Exception error) {

    }

    @Override
    public void onAddressesSuggested(List<Address> addresses) {

    }

    @Override
    public void setActive(Boolean active, String action) {
       if( active ){
           presenter.active( action );
       }else{
           presenter.inactive();
       }
    }

    @Override
    public Activity getActivity() {
        return mock(Activity.class);
    }
}
