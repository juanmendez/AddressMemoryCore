package info.juanmendez.addressmemorycore.vp.vpPhoto;

import javax.inject.Inject;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.dependencies.PhotoService;
import info.juanmendez.addressmemorycore.models.MapMemoryException;
import info.juanmendez.addressmemorycore.modules.MapModuleBase;
import info.juanmendez.addressmemorycore.vp.Presenter;

/**
 * Created by Juan Mendez on 8/14/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class PhotoPresenter implements Presenter<PhotoViewModel,PhotoView> {

    @Inject
    PhotoService photoService;

    @Inject
    AddressProvider addressProvider;

    @Inject
    NavigationService navigationService;

    private PhotoView view;
    private PhotoViewModel viewModel;
    private boolean rotated;

    @Override
    public PhotoViewModel getViewModel(PhotoView photoView) {
        this.view = photoView;
        MapModuleBase.getInjector().inject(this);
        return viewModel = new PhotoViewModel();
    }

    //view requests to pick photo from public gallery
    public void requestPickPhoto(){
        photoService.pickPhoto(view.getActivity()).subscribe(photoLocation -> {
            viewModel.setPhoto(photoLocation);
        }, throwable -> {
            viewModel.setPhotoException(new MapMemoryException(throwable.getMessage()));
        });
    }

    //view requests to take a photo
    public void requestTakePhoto(){
         photoService.takePhoto(view.getActivity()).subscribe(photoLocation -> {
            viewModel.setPhoto( photoLocation );
        }, throwable -> {
            viewModel.setPhotoException(new MapMemoryException(throwable.getMessage()));
        });
    }

    public void confirmPhoto(){
        if( !viewModel.getPhoto().isEmpty() ){
            viewModel.getAddress().setPhotoLocation(viewModel.getPhoto());
        }

        navigationService.goBack();
    }

    public void deletePhoto(){
        photoService.deletePhoto( viewModel.getAddress().getPhotoLocation() );
        viewModel.clearPhoto();
    }

    @Override
    public void active(String params ) {
        if( !rotated ){
            viewModel.setAddress( addressProvider.getSelectedAddress() );
        }
    }

    @Override
    public void inactive(Boolean rotated) {
        this.rotated = rotated;
    }
}