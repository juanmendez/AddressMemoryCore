package info.juanmendez.mapmemorycore.modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.juanmendez.mapmemorycore.CoreApp;
import info.juanmendez.mapmemorycore.dependencies.RealmProvider;

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
    public RealmProvider getRealmProvider(){
        return app.getRealmProvider();
    }

    @Singleton
    @Provides
    public Application getApplication(){
        return app.getApplication();
    }

    public static MapCoreComponent getComponent(){
        return component;
    }
}
