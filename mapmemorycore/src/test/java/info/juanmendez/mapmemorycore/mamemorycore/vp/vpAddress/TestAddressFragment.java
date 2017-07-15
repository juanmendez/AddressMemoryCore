package info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddress;

import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.vp.vpAddress.AddressPresenter;
import info.juanmendez.mapmemorycore.vp.vpAddress.AddressFragment;


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
    public void showAddress(Address address, Boolean online) {
        System.out.println( "address: " + address.getName() + " populates in here" );
    }

    @Override
    public void setActive(Boolean active, String action) {
       if( active ){
           presenter.active( action );
       }else{
           presenter.inactive();
       }
    }
}
