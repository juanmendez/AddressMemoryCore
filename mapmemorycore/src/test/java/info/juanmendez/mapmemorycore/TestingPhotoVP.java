package info.juanmendez.mapmemorycore;

import android.app.Activity;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.io.File;

import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.PhotoService;
import info.juanmendez.mapmemorycore.mamemorycore.TestApp;
import info.juanmendez.mapmemorycore.mamemorycore.module.DaggerMapCoreComponent;
import info.juanmendez.mapmemorycore.mamemorycore.module.MapCoreModule;
import info.juanmendez.mapmemorycore.modules.MapModuleBase;
import info.juanmendez.mapmemorycore.vp.vpPhoto.PhotoPresenter;
import info.juanmendez.mapmemorycore.vp.vpPhoto.PhotoView;
import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
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
    private AddressProvider addressProvider;
    private String fileLocation = "absolute_path";


    @Before
    public void before() throws Exception {
        MapModuleBase.setInjector( DaggerMapCoreComponent.builder().mapCoreModule(new MapCoreModule(new TestApp())).build() );

        viewMocked = mock( PhotoView.class );
        presenter = new PhotoPresenter();
        presenter.register(viewMocked);

        photoServiceMocked = Whitebox.getInternalState(presenter, "photoService");
        addressProvider = Whitebox.getInternalState(presenter, "addressProvider");

        //make each mocked object answer with positive results such as networkService.isConnected() returning true.
        applySuccessfulResults();
    }

    @Test
    public void pickPhoto(){
        presenter.active("");
        reset( viewMocked );
        presenter.requestPickPhoto();
        verify( viewMocked ).onPhotoSelected( any(File.class));
        Assert.assertNotNull( addressProvider.getSelectedAddress().getPhotoLocation() );
    }

    @Test
    public void takePhoto(){
        presenter.active("");
        reset( viewMocked );
        presenter.requestTakePhoto();
        verify( viewMocked ).onPhotoSelected( any(File.class));
        Assert.assertNotNull( addressProvider.getSelectedAddress().getPhotoLocation() );
    }


    private void applySuccessfulResults(){

        doAnswer(invocation -> {
            File file = mock(File.class);
            doReturn( fileLocation ).when( file ).getAbsolutePath();

            return Observable.just( file);

        }).when(photoServiceMocked).pickPhoto(any(Activity.class));

        doAnswer(invocation -> {
            File file = mock(File.class);
            doReturn( fileLocation ).when( file ).getAbsolutePath();

            return Observable.just( file);

        }).when(photoServiceMocked).takePhoto(any(Activity.class));
    }
}