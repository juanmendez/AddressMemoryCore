package info.juanmendez.mapmemorycore.vp.vpPhoto;

import java.io.File;

import javax.inject.Inject;

import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.NavigationService;
import info.juanmendez.mapmemorycore.dependencies.PhotoService;
import info.juanmendez.mapmemorycore.models.MapMemoryException;
import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.models.SubmitError;
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

    @Override
    public PhotoPresenter register(PhotoView photoView) {
        this.view = photoView;
        MapCoreModule.getComponent().inject(this);
        return this;
    }

    //view requests to pick photo from public gallery
    public void requestPickPhoto(){
        fileSubscription = photoService.pickPhoto(view.getActivity()).subscribe(file -> {
            setPhotoSelected( file );
            view.onPhotoSelected( file );
        }, throwable -> {
            view.onPhotoError( new MapMemoryException(throwable.getMessage()));
        });
    }

    //view requests to take a photo
    public void requestTakePhoto(){
        fileSubscription = photoService.takePhoto(view.getActivity()).subscribe(file -> {
            setPhotoSelected( file );
            view.onPhotoSelected( file );
        }, throwable -> {
            view.onPhotoError( new MapMemoryException(throwable.getMessage()));
        });
    }

    @Override
    public void active(String action) {
        selectedAddress = addressProvider.getSelectedAddress();

        if( selectedAddress != null && !SubmitError.emptyOrNull(selectedAddress.getPhotoLocation())){
            view.onPhotoSelected(new File( selectedAddress.getPhotoLocation()));
        }
    }

    @Override
    public void inactive() {
    }

    private void setPhotoSelected(File file ){
        ShortAddress address = addressProvider.getSelectedAddress();

        if( address == null ){
            addressProvider.selectAddress( new ShortAddress());
            address = addressProvider.getSelectedAddress();
        }

        address.setPhotoLocation( file.getAbsolutePath() );
    }
}
