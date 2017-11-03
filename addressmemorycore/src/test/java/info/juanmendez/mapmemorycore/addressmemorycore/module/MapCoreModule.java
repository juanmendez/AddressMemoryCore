package info.juanmendez.mapmemorycore.addressmemorycore.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.AddressService;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.PhotoService;
import info.juanmendez.addressmemorycore.dependencies.WidgetService;
import info.juanmendez.mapmemorycore.addressmemorycore.TestApp;

import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

@Module
public class MapCoreModule {
    private TestApp mApp;

    public MapCoreModule(TestApp app) {
        mApp = app;
    }

    @Singleton
    @Provides
    public AddressProvider getAddressProvider(){
        return mApp.getAddressProvider();
    }

    @Provides
    public AddressService getAddressService(){
        return mock( AddressService.class );
    }

    @Singleton
    @Provides
    public Application getApplication(){
        return mApp.getApplication();
    }

    @Singleton
    @Provides
    public PhotoService getPhotoService(){
        return mock( PhotoService.class );
    }

    @Provides
    public NetworkService getNetworkService(){
        return mock( NetworkService.class );
    }

    @Provides
    @Singleton
    public NavigationService getNavigationService(){
        return mock(NavigationService.class);
    }

    @Provides
    @Singleton
    public WidgetService getWidgetService(){
        return mock(WidgetService.class);
    }
}
