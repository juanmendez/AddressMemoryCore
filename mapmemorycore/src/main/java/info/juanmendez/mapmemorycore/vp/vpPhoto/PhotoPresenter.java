package info.juanmendez.mapmemorycore.vp.vpPhoto;

import java.io.File;

import javax.inject.Inject;

import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.NavigationService;
import info.juanmendez.mapmemorycore.dependencies.PhotoService;
import info.juanmendez.mapmemorycore.models.MapMemoryException;
import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.vp.ViewPresenter;
import rx.Subscription;

/**
 * Created by Juan Mendez on 8/14/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class PhotoPresenter implements ViewPresenter<PhotoPresenter,PhotoView>{

    @Inject
    PhotoService photoService;

    @Inject
    AddressProvider addressProvider;

    @Inject
    NavigationService navigationService;

    private PhotoView view;
    private Subscription fileSubscription;
    private ShortAddress selectedAddress;
    private File photoTaken;

    @Override
    public PhotoPresenter register(PhotoView photoView) {
        this.view = photoView;
        MapCoreModule.getComponent().inject(this);
        return this;
    }

    //view requests to pick photo from public gallery
    public void requestPickPhoto(){
        fileSubscription = photoService.pickPhoto(view.getActivity()).subscribe(file -> {
            photoTaken = file;
            view.onPhotoSelected( file );
        }, throwable -> {
            view.onPhotoError( new MapMemoryException(throwable.getMessage()));
        });
    }

    //view requests to take a photo
    public void requestTakePhoto(){
        fileSubscription = photoService.takePhoto(view.getActivity()).subscribe(file -> {
            photoTaken = file;
            view.onPhotoSelected( file );
        }, throwable -> {
            view.onPhotoError( new MapMemoryException(throwable.getMessage()));
        });
    }

    public void imageConfirmed(){
        if( selectedAddress != null && photoTaken != null ){
            selectedAddress.setPhotoLocation( photoTaken.getAbsolutePath() );
        }

        navigationService.goBack();
    }

    @Override
    public void active(String action) {
        selectedAddress = addressProvider.getSelectedAddress();

        if( photoTaken == null && selectedAddress != null ){
            photoTaken = new File( selectedAddress.getPhotoLocation() );
        }

        view.onPhotoSelected(photoTaken);
    }

    @Override
    public void inactive(Boolean rotated) {
        if(!rotated)
            photoTaken = null;
    }

    public void deleteImage() {
        if( photoTaken !=null ){
            selectedAddress.setPhotoLocation("");
            photoTaken = new File("");
            view.onPhotoSelected( photoTaken );
        }
    }
}
