package info.juanmendez.addressmemorycore.vp.vpPhoto;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import info.juanmendez.addressmemorycore.BR;
import info.juanmendez.addressmemorycore.models.ShortAddress;

/**
 * Created by Juan Mendez on 10/5/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class PhotoViewModel extends BaseObservable {
    private ShortAddress address = new ShortAddress();
    private Exception photoException;
    private String photo = "";

    //<editor-fold desc="address">
    @Bindable
    public ShortAddress getAddress() {
        return address;
    }

    public void setAddress(ShortAddress address) {
        this.address = address;
        notifyPropertyChanged(BR.address);

        setPhoto( address.getPhotoLocation() );
    }

    public void clearPhoto(){
        photo = "";
        notifyPropertyChanged(BR.photo);
    }
    //</editor-fold>

    //<editor-fold desc="photo">
    @Bindable
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
        notifyPropertyChanged(BR.photo);
    }
    //</editor-fold>

    //<editor-fold desc="photoException">
    @Bindable
    public Exception getPhotoException() {
        return photoException;
    }

    public void setPhotoException(Exception photoException) {
        this.photoException = photoException;
        notifyPropertyChanged(BR.addressException);
    }
    //</editor-fold>
}
