import android.app.Activity;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.io.File;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.dependencies.PhotoService;
import info.juanmendez.addressmemorycore.modules.MapModuleBase;
import info.juanmendez.addressmemorycore.vp.vpPhoto.PhotoPresenter;
import info.juanmendez.addressmemorycore.vp.vpPhoto.PhotoView;
import info.juanmendez.addressmemorycore.vp.vpPhoto.PhotoViewModel;
import info.juanmendez.mapmemorycore.addressmemorycore.TestApp;
import info.juanmendez.mapmemorycore.addressmemorycore.module.DaggerMapCoreComponent;
import info.juanmendez.mapmemorycore.addressmemorycore.module.MapCoreModule;
import rx.Observable;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;
/**
 * Created by Juan Mendez on 8/14/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestingPhotoVP {

    private PhotoView viewMocked;
    private PhotoPresenter presenter;
    private PhotoService photoServiceMocked;
    NavigationService navigationService;
    private AddressProvider addressProvider;
    private String cameraLocation = "/gallery/oldPhoto.png";
    private String galleryLocation = "/camera/photo.png";

    private PhotoViewModel viewModel;
    private File photoTaken = new File(cameraLocation);
    private File photoPicked = new File(galleryLocation);

    @Before
    public void before() throws Exception {
        MapModuleBase.setInjector( DaggerMapCoreComponent.builder().mapCoreModule(new MapCoreModule(new TestApp())).build() );

        viewMocked = mock( PhotoView.class );
        presenter = new PhotoPresenter();
        viewModel = presenter.getViewModel(viewMocked);

        addressProvider = Whitebox.getInternalState(presenter, "addressProvider");
        photoServiceMocked = Whitebox.getInternalState(presenter, "photoService");
        navigationService = Whitebox.getInternalState(presenter, "navigationService");

        //make each mocked object answer with positive results such as networkService.isConnected() returning true.
        applySuccessfulResults();
    }

    @Test
    public void pickPhoto(){
        presenter.active("");
        reset( viewMocked );

        //address in presenter is just blank
        assertTrue( viewModel.getAddress().getPhotoLocation().isEmpty() );

        presenter.requestPickPhoto();
        assertEquals( viewModel.getPhoto(), photoPicked);

        //ok, then confirm photo
        presenter.imageConfirmed();

        //so selectedAddress must have the current photo's file path
        assertEquals( viewModel.getAddress().getPhotoLocation(), photoPicked.toString() );
        verify( navigationService ).goBack();
        presenter.inactive(false);
    }

    @Test
    public void clearPhoto(){

        presenter.active("");
        reset( viewMocked );

        presenter.requestPickPhoto();
        assertEquals( photoPicked, viewModel.getPhoto());
        presenter.imageConfirmed();
        assertFalse( viewModel.getAddress().getPhotoLocation().isEmpty() );


        viewModel.clearPhoto();
        presenter.imageConfirmed();
        assertTrue( viewModel.getAddress().getPhotoLocation().isEmpty() );
        verify( navigationService, times(2) ).goBack();
        presenter.inactive(false);
    }

    @Test
    public void takePhoto(){
        presenter.active("");
        reset( viewMocked );

        presenter.requestTakePhoto();
        assertEquals( viewModel.getPhoto(), photoTaken);

        presenter.inactive(false);
    }

    private void applySuccessfulResults(){

        doAnswer(invocation -> {
            return Observable.just(photoPicked);

        }).when(photoServiceMocked).pickPhoto(any(Activity.class));

        doAnswer(invocation -> {
            return Observable.just(photoTaken);
        }).when(photoServiceMocked).takePhoto(any(Activity.class));
    }
}