package info.juanmendez.addressmemorycore.vp.vpSuggest;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.addressmemorycore.BR;
import info.juanmendez.addressmemorycore.models.ShortAddress;


/**
 * Created by Juan Mendez on 10/3/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * This viewModel glues SuggestPresenter with SuggestView
 */
public class SuggestViewModel extends BaseObservable {

    //given address, we pull its address1 as a bindable
    private ShortAddress mSelectedAddress = new ShortAddress();
    private ShortAddress mPickedAddress = new ShortAddress();

    //matching addresses.
    private List<ShortAddress> mMatchingAddresses = new ArrayList<>();

    //any error?
    private Exception mAddressException;

    private String mLastAddressTyped = "";

    @Bindable public final ObservableBoolean keyboardOn = new ObservableBoolean(false);

    public void setSelectedAddress(ShortAddress selectedAddress) {
        mSelectedAddress = selectedAddress;
        notifyPropertyChanged(BR.addressEdited);
    }

    //<editor-fold desc="mMatchingAddresses">
    @Bindable
    public List<ShortAddress> getMatchingAddresses() {
        return mMatchingAddresses;
    }

    public void setMatchingAddresses(List<ShortAddress> addresses) {
        mMatchingAddresses.clear();
        mMatchingAddresses.addAll(addresses);
        notifyPropertyChanged(BR.matchingAddresses);
    }

    public void clearMatchingResults(){
        mMatchingAddresses.clear();
        notifyPropertyChanged(BR.matchingAddresses);
    }
    //</editor-fold>


    //<editor-fold desc="mPickedAddress">
    @Bindable
    public ShortAddress getPickedAddress() {
        return mPickedAddress;
    }

    public void setPickedAddress(ShortAddress pickedAddress) {
        mPickedAddress = pickedAddress;
        notifyPropertyChanged(BR.pickedAddress);
    }
    //</editor-fold>

    //<editor-fold desc="addressEdited">
    @Bindable
    public String getAddressEdited() {
        return mSelectedAddress.getAddress1();
    }

    public void setAddressEdited(String addressEdited) {
        mLastAddressTyped = addressEdited;
        mSelectedAddress.setAddress1(addressEdited);
        notifyPropertyChanged(BR.addressEdited);
    }
    //</editor-fold>

    //<editor-fold desc="mAddressException">
    @Bindable
    public Exception getAddressException() {
        return mAddressException;
    }

    public void setAddressException(Exception addressException) {
        mAddressException = addressException;
        notifyPropertyChanged(BR.addressException);
    }
    //</editor-fold>
}
