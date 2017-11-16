package info.juanmendez.addressmemorycore.vp.vpPhoto;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

import info.juanmendez.addressmemorycore.BR;
import info.juanmendez.addressmemorycore.models.ShortAddress;

/**
 * Created by Juan Mendez on 10/5/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class PhotoViewModel extends BaseObservable {
    private ShortAddress mAddress = new ShortAddress();
    private Exception mPhotoException;
    private String mPhoto = "";

    @Bindable public final ObservableBoolean isModified = new ObservableBoolean(false);

    //<editor-fold desc="mAddress">
    @Bindable
    public ShortAddress getAddress() {
        return mAddress;
    }

    public void setAddress(ShortAddress address) {
        mAddress = address;
        notifyPropertyChanged(BR.address);
        setPhoto( address.getPhotoLocation() );
        isModified.set(false);
    }

    public void clearPhoto(){
        mPhoto = "";
        notifyPropertyChanged(BR.photo);
    }
    //</editor-fold>

    //<editor-fold desc="mPhoto">
    @Bindable
    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
        notifyPropertyChanged(BR.photo);
    }
    //</editor-fold>

    //<editor-fold desc="mPhotoException">
    @Bindable
    public Exception getPhotoException() {
        return mPhotoException;
    }

    public void setPhotoException(Exception photoException) {
        mPhotoException = photoException;
        notifyPropertyChanged(BR.addressException);
    }
    //</editor-fold>
}
