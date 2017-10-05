package info.juanmendez.addressmemorycore.vp.vpPhoto;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.File;

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
    private String photoTaken;

    //<editor-fold desc="address">
    @Bindable
    public ShortAddress getAddress() {
        return address;
    }

    public void setAddress(ShortAddress address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    public void confirmPhoto(){
        if( photoTaken != null ){
            address.setPhotoLocation( photoTaken );
            notifyPropertyChanged(BR.address);
        }
    }

    public void clearPhoto(){
        photoTaken = "";
    }
    //</editor-fold>

    //<editor-fold desc="photo">
    @Bindable
    public File getPhoto() {
        return new File( photoTaken );
    }

    public void setPhoto(File photo) {
        photoTaken = photo.toString();
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
