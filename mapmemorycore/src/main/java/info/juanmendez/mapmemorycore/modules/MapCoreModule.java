package info.juanmendez.mapmemorycore.modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.juanmendez.mapmemorycore.CoreApp;
import info.juanmendez.mapmemorycore.dependencies.Navigation;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AddressService;
import info.juanmendez.mapmemorycore.dependencies.db.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.network.NetworkService;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

@Module
public class MapCoreModule {

    private static MapCoreComponent component;
    private CoreApp app;

    public static void setApp(CoreApp app ){
        component = DaggerMapCoreComponent.builder().mapCoreModule(new MapCoreModule(app)).build();
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
    public NetworkService getNetworkService(){
        return app.getNeworkService();
    }

    @Singleton
    @Provides
    public Navigation getNavigation(){
        return app.getNavigation();
    }

    public static MapCoreComponent getComponent(){
        return component;
    }
}
