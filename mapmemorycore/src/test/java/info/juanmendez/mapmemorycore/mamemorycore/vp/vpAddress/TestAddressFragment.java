package info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddress;

import android.app.Activity;

import java.io.File;

import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.vp.vpAddress.AddressPresenter;
import info.juanmendez.mapmemorycore.vp.vpAddress.AddressView;

import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by Juan Mendez on 6/28/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class TestAddressFragment implements AddressView {
    AddressPresenter presenter;

    public TestAddressFragment() {
        presenter = new AddressPresenter();
        presenter.register(this);
    }

    public AddressPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onNetworkStatus(Boolean online) {

    }

    @Override
    public void onAddressResult(ShortAddress address) {

    }

    @Override
    public void onAddressError(Exception error) {

    }

    @Override
    public void onPhotoSelected(File photo) {

    }

    @Override
    public void canDelete(Boolean allow) {

    }

    @Override
    public void canSubmit(Boolean allow) {

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
