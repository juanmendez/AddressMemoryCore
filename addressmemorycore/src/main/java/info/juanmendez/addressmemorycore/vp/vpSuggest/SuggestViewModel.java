package info.juanmendez.addressmemorycore.vp.vpSuggest;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

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
    private ShortAddress selectedAddress = new ShortAddress();
    private ShortAddress pickedAddress = new ShortAddress();

    //matching addresses.
    private List<ShortAddress> matchingAddresses = new ArrayList();

    //any error?
    private Exception addressException;

    private String lastAddressTyped = "";

    public void setSelectedAddress(ShortAddress selectedAddress) {
        this.selectedAddress = selectedAddress;
        notifyPropertyChanged(BR.addressEdited);
    }

    //<editor-fold desc="matchingAddresses">
    @Bindable
    public List<ShortAddress> getMatchingAddresses() {
        return matchingAddresses;
    }

    public void setMatchingAddresses(List<ShortAddress> addresses) {
        this.matchingAddresses.clear();
        this.matchingAddresses.addAll(addresses);
        notifyPropertyChanged(BR.matchingAddresses);
    }

    public void clearMatchingResults(){
        this.matchingAddresses.clear();
        notifyPropertyChanged(BR.matchingAddresses);
    }
    //</editor-fold>


    //<editor-fold desc="pickedAddress">
    @Bindable
    public ShortAddress getPickedAddress() {
        return pickedAddress;
    }

    public void setPickedAddress(ShortAddress pickedAddress) {
        this.pickedAddress = pickedAddress;
        notifyPropertyChanged(BR.pickedAddress);
    }
    //</editor-fold>

    //<editor-fold desc="addressEdited">
    @Bindable
    public String getAddressEdited() {
        return selectedAddress.getAddress1();
    }

    public void setAddressEdited(String addressEdited) {
        lastAddressTyped = addressEdited;
        selectedAddress.setAddress1(addressEdited);
        notifyPropertyChanged(BR.addressEdited);
    }
    //</editor-fold>

    //<editor-fold desc="addressException">
    @Bindable
    public Exception getAddressException() {
        return addressException;
    }

    public void setAddressException(Exception addressException) {
        this.addressException = addressException;
        notifyPropertyChanged(BR.addressException);
    }
    //</editor-fold>
}
