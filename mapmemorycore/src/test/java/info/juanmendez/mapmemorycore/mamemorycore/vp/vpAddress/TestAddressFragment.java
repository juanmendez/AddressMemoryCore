package info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddress;

import android.app.Activity;

import java.io.File;
import java.util.List;

import info.juanmendez.mapmemorycore.models.MapAddress;
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
    public void onAddressResult(MapAddress address, Boolean online) {

    }

    @Override
    public void onAddressError(Exception error) {

    }

    @Override
    public void onAddressesSuggested(List<MapAddress> addresses) {

    }

    @Override
    public void onPhotoSelected(File photo) {

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
