package info.juanmendez.mapmemorycore.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.juanmendez.mapmemorycore.CoreApp;
import info.juanmendez.mapmemorycore.services.AddressProvider;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

@Module
public class MapCoreModule {

    private static MapCoreComponent component;
    private CoreApp app;

    public MapCoreModule(CoreApp app) {
        this.app = app;
    }

    public static void setApp(CoreApp app ){
        component = DaggerMapCoreComponent.builder().mapCoreModule(new MapCoreModule(app)).build();
    }

    @Singleton
    @Provides
    public AddressProvider getAddressAdapter(){
        return app.getAddressProvider();
    }

    public static MapCoreComponent getComponent(){
        return component;
    }
}
