package info.juanmendez.addressmemorycore.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

import java.io.File;

import info.juanmendez.addressmemorycore.BR;


/**
 * Created by Juan Mendez on 9/27/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class AddressViewModel extends BaseObservable {
    @Bindable public final ObservableBoolean isOnline = new ObservableBoolean(false);

    @Bindable public final ObservableBoolean canDelete = new ObservableBoolean(false);

    @Bindable public final ObservableBoolean canSubmit = new ObservableBoolean(false);

    private ShortAddress address = new ShortAddress();
    private File photoSelected;
    private Exception addressException;

    //<editor-fold desc="address">
    @Bindable
    public ShortAddress getAddress() {
        return address;
    }

    public void setAddress(ShortAddress address) {
        this.address = address;
        notifyAddress();
    }

    public void notifyAddress(){
        notifyPropertyChanged(BR.address);
    }
    //</editor-fold>

    //<editor-fold desc="photoSelected">
    @Bindable
    public File getPhotoSelected() {
        return photoSelected;
    }

    public void setPhotoSelected(File photoSelected) {
        this.photoSelected = photoSelected;
        notifyPropertyChanged(BR.photoSelected);
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

    //<editor-fold desc="Address values">
    public String getAddress1() {
        return address.getAddress1();
    }

    public void setAddress1(String address1) {
        address.setAddress1( address1 );
        notifyAddress();
    }

    public String getAddress2() {
        return address.getAddress2();
    }

    public void setAddress2(String address2) {
        address.setAddress2( address2 );
        notifyAddress();
    }
    //</editor-fold>

    //<editor-fold desc="photo">
    public String getPhoto() {
        return address.getPhotoLocation();
    }

    public void setPhoto(String photo) {
        address.setPhotoLocation( photo );
        notifyAddress();
    }
    //</editor-fold>

    //<editor-fold desc="name">
    public void setName(String name) {
        address.setName(name);
        notifyAddress();
    }

    public String getName() {
        return address.getName();
    }
    //</editor-fold>
}