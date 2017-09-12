package info.juanmendez.mapmemorycore.mamemorycore.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.AddressService;
import info.juanmendez.mapmemorycore.dependencies.NavigationService;
import info.juanmendez.mapmemorycore.dependencies.NetworkService;
import info.juanmendez.mapmemorycore.dependencies.PhotoService;
import info.juanmendez.mapmemorycore.dependencies.WidgetService;
import info.juanmendez.mapmemorycore.mamemorycore.TestApp;

import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

@Module
public class MapCoreModule {
    private TestApp app;

    public MapCoreModule(TestApp app) {
        this.app = app;
    }

    @Singleton
    @Provides
    public AddressProvider getAddressProvider(){
        return app.getAddressProvider();
    }

    @Provides
    public AddressService getAddressService(){
        return mock( AddressService.class );
    }

    @Singleton
    @Provides
    public Application getApplication(){
        return app.getApplication();
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
