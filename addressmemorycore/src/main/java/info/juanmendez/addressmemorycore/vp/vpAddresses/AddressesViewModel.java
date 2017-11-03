package info.juanmendez.addressmemorycore.vp.vpAddresses;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.List;

import info.juanmendez.addressmemorycore.BR;
import info.juanmendez.addressmemorycore.models.ShortAddress;


/**
 * Created by Juan Mendez on 10/6/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class AddressesViewModel extends BaseObservable {
    private ShortAddress mSelectedAddress;
    private List<ShortAddress> mStreamingAddresses;

    @Bindable
    public List<ShortAddress> getStreamingAddresses() {
        return mStreamingAddresses;
    }

    public void setStreamingAddresses(List<ShortAddress> streamingAddresses) {
        mStreamingAddresses = streamingAddresses;
        notifyPropertyChanged(BR.streamingAddresses);
    }

    @Bindable
    public ShortAddress getSelectedAddress() {
        return mSelectedAddress;
    }

    public void setSelectedAddress(ShortAddress selectedAddress) {
        mSelectedAddress = selectedAddress;
        notifyPropertyChanged(BR.selectedAddress);
    }
}