package info.juanmendez.addressmemorycore.vp.vpAddress;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import info.juanmendez.addressmemorycore.BR;
import info.juanmendez.addressmemorycore.models.ShortAddress;


/**
 * Created by Juan Mendez on 9/27/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class AddressViewModel extends BaseObservable {
    @Bindable public final ObservableBoolean isOnline = new ObservableBoolean(false);

    @Bindable public final ObservableBoolean canDelete = new ObservableBoolean(false);

    @Bindable public final ObservableBoolean canSubmit = new ObservableBoolean(false);

    @Bindable public final ObservableBoolean isGeoOn = new ObservableBoolean(false);

    private String commuteType = "";
    private Boolean commuteExpressway = true;
    private Boolean commuteTolls = true;


    private ShortAddress address = new ShortAddress();
    private Exception addressException;
    private int[] brs = new int[]{BR.address, BR.address1, BR.address2, BR.name, BR.photo};

    //<editor-fold desc="address">
    @Bindable
    public ShortAddress getAddress() {
        return address;
    }

    public void setAddress(ShortAddress address) {
        this.address = address;
        notifyPropertyChanged(BR._all);
    }
    //</editor-fold>

    public void notifyAddress(){
        notifyPropertyChanged(BR.address);
    }

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
    @Bindable
    public String getAddress1() {
        return address.getAddress1();
    }

    public void setAddress1(String address1) {
        address.setAddress1( address1 );
        notifyPropertyChanged(BR.address1);
    }

    @Bindable
    public String getAddress2() {
        return address.getAddress2();
    }

    public void setAddress2(String address2) {
        address.setAddress2( address2 );
        notifyPropertyChanged(BR.address2);
    }
    //</editor-fold>

    //<editor-fold desc="photo">
    @Bindable
    public String getPhoto() {
        return address.getPhotoLocation();
    }

    public void setPhoto(String photo) {
        address.setPhotoLocation( photo );
        notifyPropertyChanged(BR.photo);
    }
    //</editor-fold>

    //<editor-fold desc="name">
    public void setName(String name) {
        address.setName(name);
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getName() {
        return address.getName();
    }
    //</editor-fold>
}