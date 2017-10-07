package info.juanmendez.addressmemorycore.vp.vpAddress;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.addressmemorycore.BR;
import info.juanmendez.addressmemorycore.models.Commute;
import info.juanmendez.addressmemorycore.models.ShortAddress;


/**
 * Created by Juan Mendez on 9/27/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class AddressViewModel extends BaseObservable {

    public static List<Integer> addressEdits = new ArrayList<>();
    static {
        addressEdits.add(BR._all);
        addressEdits.add(BR.address);
        addressEdits.add(BR.address1);
        addressEdits.add(BR.address2);
        addressEdits.add(BR.name);
        addressEdits.add(BR.photo);
    }

    public static List<Integer> commuteEdits = new ArrayList<>();
    static{
        commuteEdits.add(BR.commuteType);
        commuteEdits.add(BR.avoidTolls);
        commuteEdits.add(BR.avoidXpressway);
    }

    @Bindable public final ObservableBoolean isOnline = new ObservableBoolean(false);

    @Bindable public final ObservableBoolean canDelete = new ObservableBoolean(false);

    @Bindable public final ObservableBoolean canSubmit = new ObservableBoolean(false);

    @Bindable public final ObservableBoolean isGeoOn = new ObservableBoolean(false);

    private String commuteType = "";
    private Boolean avoidXpressway = false;
    private Boolean commuteAvoidTolls = false;


    private ShortAddress address = new ShortAddress();
    private Exception addressException;




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

    //<editor-fold desc="commute.Attrs">
    @Bindable
    public String getCommuteType() {
        return getCommute().getType();
    }

    public void setCommuteType(String commuteType) {
        getCommute().setType( commuteType );
        notifyPropertyChanged(BR.commuteType);
    }

    @Bindable
    public Boolean getAvoidXpressway() {
        return getCommute().getAvoidXpressway();
    }

    public void setAvoidXpressway(Boolean avoidXpressway) {
        getCommute().setAvoidXpressway(avoidXpressway);
        notifyPropertyChanged(BR.avoidXpressway);
    }

    @Bindable
    public Boolean getAvoidTolls() {
        return getCommute().getAvoidTolls();
    }

    public void setAvoidTolls(Boolean commuteAvoidTolls) {
        getCommute().setAvoidTolls(commuteAvoidTolls);
        notifyPropertyChanged(BR.avoidTolls);
    }

    private Commute getCommute(){
        return address.getCommute();
    }
    //</editor-fold>
}