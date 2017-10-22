import android.app.Activity;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

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
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
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

    private PhotoView photoView;
    private PhotoPresenter presenter;
    private PhotoService photoServiceMocked;
    private NavigationService navigationService;
    private AddressProvider addressProvider;
    private String photoTaken = "/gallery/oldPhoto.png";
    private String photoPicked = "/camera/photo.png";

    private PhotoViewModel viewModel;

    @Before
    public void before() throws Exception {
        MapModuleBase.setInjector( DaggerMapCoreComponent.builder().mapCoreModule(new MapCoreModule(new TestApp())).build() );

        photoView = mock( PhotoView.class );
        presenter = new PhotoPresenter();
        viewModel = presenter.getViewModel(photoView);

        addressProvider = Whitebox.getInternalState(presenter, "addressProvider");
        photoServiceMocked = Whitebox.getInternalState(presenter, "photoService");
        navigationService = Whitebox.getInternalState(presenter, "navigationService");

        //make each mocked object answer with positive results such as networkService.isConnected() returning true.
        applySuccessfulResults();
    }

    @Test
    public void pickPhoto(){
        presenter.active("");
        reset(photoView);

        //address in presenter is just blank
        assertTrue( viewModel.getAddress().getPhotoLocation().isEmpty() );

        presenter.requestPickPhoto();
        assertEquals( viewModel.getPhoto(), photoPicked);

        //ok, then confirm photo
        presenter.confirmPhoto();

        //so address pojo must have the same photo
        assertEquals( viewModel.getAddress().getPhotoLocation(), photoPicked );
        verify( navigationService ).goBack();
        presenter.inactive(false);
    }

    @Test
    public void clearPhoto(){

        presenter.active("");
        reset(photoView);

        //you pick a photo, then you confirm you want it
        //therefore the address pojo has that photo
        presenter.requestPickPhoto();
        assertEquals( photoPicked, viewModel.getPhoto());
        presenter.confirmPhoto();
        assertFalse( viewModel.getAddress().getPhotoLocation().isEmpty() );


        viewModel.clearPhoto();
        presenter.confirmPhoto();
        assertTrue( viewModel.getAddress().getPhotoLocation().isEmpty() );
        verify( navigationService, times(2) ).goBack();
        presenter.inactive(false);
    }

    @Test
    public void takePhoto(){
        presenter.active("");
        reset(photoView);

        //oh, so you take a selfie, then lets see after confirm you did
        presenter.requestTakePhoto();
        presenter.confirmPhoto();
        assertEquals( viewModel.getPhoto(), photoTaken);

        presenter.inactive(false);
    }

    private void applySuccessfulResults(){


        doReturn( "resource_string" ).when(photoView).getString( anyInt() );

        doAnswer(invocation -> {
            return Observable.just(photoPicked);

        }).when(photoServiceMocked).pickPhoto(any(Activity.class));

        doAnswer(invocation -> {
            return Observable.just(photoTaken);
        }).when(photoServiceMocked).takePhoto(any(Activity.class));
    }
}