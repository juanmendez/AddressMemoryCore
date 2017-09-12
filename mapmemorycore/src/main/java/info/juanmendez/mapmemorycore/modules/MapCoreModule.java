package info.juanmendez.mapmemorycore.modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.juanmendez.mapmemorycore.CoreApp;
import info.juanmendez.mapmemorycore.dependencies.AddressService;
import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.NavigationService;
import info.juanmendez.mapmemorycore.dependencies.NetworkService;
import info.juanmendez.mapmemorycore.dependencies.PhotoService;
import info.juanmendez.mapmemorycore.dependencies.WidgetService;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

@Module
public class MapCoreModule {
    private CoreApp app;

    public CoreApp getApp() {
        return app;
    }

    public MapCoreModule(CoreApp app) {
        this.app = app;
    }

    @Singleton
    @Provides
    public AddressProvider getAddressProvider(){
        return app.getAddressProvider();
    }

    @Provides
    public AddressService getAddressService(){
        return  app.getAddressService();
    }

    @Singleton
    @Provides
    public Application getApplication(){
        return app.getApplication();
    }

    @Singleton
    @Provides
    public PhotoService getPhotoService(){
        return app.getPhotoService();
    }

    @Provides
    public NetworkService getNetworkService(){
        return app.getNetworkService();
    }

    @Provides
    @Singleton
    public NavigationService getNavigationService(){
        return app.getNavigationService();
    }

    @Provides
    @Singleton
    public WidgetService getWidgetService(){
        return app.getWidgetService();
    }
}
