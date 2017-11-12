package info.juanmendez.addressmemorycore.vp.vpPhoto;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.dependencies.PhotoService;
import info.juanmendez.addressmemorycore.dependencies.Response;
import info.juanmendez.addressmemorycore.models.AddressException;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.modules.AddressCoreModule;
import info.juanmendez.addressmemorycore.vp.Presenter;

/**
 * Created by Juan Mendez on 8/14/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class PhotoPresenter implements Presenter<PhotoViewModel,PhotoView> {

    private PhotoService mPhotoService;
    private AddressProvider mAddressProvider;
    private NavigationService mNavigationService;

    private PhotoView mView;
    private PhotoViewModel mViewModel;
    private boolean mRotated;

    public PhotoPresenter(AddressCoreModule module) {
        mPhotoService = module.getPhotoService();
        mAddressProvider = module.getAddressProvider();
        mNavigationService = module.getNavigationService();
    }

    @Override
    public PhotoViewModel getViewModel(PhotoView photoView) {
        mView = photoView;
        return mViewModel = new PhotoViewModel();
    }

    //mView requests to pick photo from public gallery
    public void requestPickPhoto(){
        mPhotoService.pickPhoto(mView.getActivity()).subscribe(photoLocation -> {
            mViewModel.setPhoto(photoLocation);
        }, throwable -> {
            mViewModel.setPhotoException(new AddressException(throwable.getMessage()));
        });
    }

    //mView requests to take a photo
    public void requestTakePhoto(){
         mPhotoService.takePhoto(mView.getActivity()).subscribe(photoLocation -> {
            mViewModel.setPhoto( photoLocation );
        }, throwable -> {
            mViewModel.setPhotoException(new AddressException(throwable.getMessage()));
        });
    }

    public void confirmPhoto(){
        if( !mViewModel.getPhoto().isEmpty() ){
            mViewModel.getAddress().setPhotoLocation(mViewModel.getPhoto());
        }else{
            mViewModel.getAddress().setPhotoLocation("");
        }

        mNavigationService.goBack();
    }

    public void deletePhoto(){

        ShortAddress selectedAddress = mViewModel.getAddress();
        mPhotoService.deletePhoto( selectedAddress.getPhotoLocation() );

        if( selectedAddress.getAddressId() > 0 ){

            selectedAddress.setPhotoLocation("");

            mAddressProvider.updateAddressAsync(selectedAddress, new Response<ShortAddress>() {

                @Override
                public void onError(Exception exception) {
                }

                @Override
                public void onResult(ShortAddress result) {
                    mAddressProvider.selectAddress( result );
                    mViewModel.setAddress(result);
                    mViewModel.clearPhoto();
                    mNavigationService.goBack();
                }
            });
        }else{
            mViewModel.clearPhoto();
            mNavigationService.goBack();
        }
    }

    @Override
    public void active(String params ) {
        if( !mRotated){
            mViewModel.setAddress( mAddressProvider.getSelectedAddress() );
        }
    }

    @Override
    public void inactive(Boolean rotated) {
        mRotated = rotated;
    }
}