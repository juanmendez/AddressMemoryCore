package info.juanmendez.addressmemorycore.vp.vpPhoto;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.dependencies.PhotoService;
import info.juanmendez.addressmemorycore.dependencies.Response;
import info.juanmendez.addressmemorycore.models.MapMemoryException;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.modules.CoreModule;
import info.juanmendez.addressmemorycore.vp.Presenter;

/**
 * Created by Juan Mendez on 8/14/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class PhotoPresenter implements Presenter<PhotoViewModel,PhotoView> {

    PhotoService photoService;
    AddressProvider addressProvider;
    NavigationService navigationService;

    private CoreModule mCoreModule;
    private PhotoView mView;
    private PhotoViewModel mViewModel;
    private boolean mRotated;

    public PhotoPresenter(CoreModule coreModule) {
        mCoreModule = coreModule;
        photoService = mCoreModule.getPhotoService();
        addressProvider = mCoreModule.getAddressProvider();
        navigationService = mCoreModule.getNavigationService();
    }

    @Override
    public PhotoViewModel getViewModel(PhotoView photoView) {
        mView = photoView;
        return mViewModel = new PhotoViewModel();
    }

    //mView requests to pick photo from public gallery
    public void requestPickPhoto(){
        photoService.pickPhoto(mView.getActivity()).subscribe(photoLocation -> {
            mViewModel.setPhoto(photoLocation);
        }, throwable -> {
            mViewModel.setPhotoException(new MapMemoryException(throwable.getMessage()));
        });
    }

    //mView requests to take a photo
    public void requestTakePhoto(){
         photoService.takePhoto(mView.getActivity()).subscribe(photoLocation -> {
            mViewModel.setPhoto( photoLocation );
        }, throwable -> {
            mViewModel.setPhotoException(new MapMemoryException(throwable.getMessage()));
        });
    }

    public void confirmPhoto(){
        if( !mViewModel.getPhoto().isEmpty() ){
            mViewModel.getAddress().setPhotoLocation(mViewModel.getPhoto());
        }else{
            mViewModel.getAddress().setPhotoLocation("");
        }

        navigationService.goBack();
    }

    public void deletePhoto(){

        ShortAddress selectedAddress = mViewModel.getAddress();
        photoService.deletePhoto( selectedAddress.getPhotoLocation() );

        if( selectedAddress.getAddressId() > 0 ){

            selectedAddress.setPhotoLocation("");

            addressProvider.updateAddressAsync(selectedAddress, new Response<ShortAddress>() {

                @Override
                public void onError(Exception exception) {
                }

                @Override
                public void onResult(ShortAddress result) {
                    addressProvider.selectAddress( result );
                    mViewModel.setAddress(result);
                    mViewModel.clearPhoto();
                    navigationService.goBack();
                }
            });
        }else{
            mViewModel.clearPhoto();
            navigationService.goBack();
        }
    }

    @Override
    public void active(String params ) {
        if( !mRotated){
            mViewModel.setAddress( addressProvider.getSelectedAddress() );
        }
    }

    @Override
    public void inactive(Boolean rotated) {
        mRotated = rotated;
    }
}