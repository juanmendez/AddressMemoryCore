package info.juanmendez.mapmemorycore.modules;

import dagger.Module;
import dagger.Provides;
import info.juanmendez.mapmemorycore.vp.CoreApp;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

@Module
public class MapMemoryModule {
    CoreApp app;

    public void setApp(CoreApp app) {
        this.app = app;
    }

    @Provides
    public CoreApp provideApp(){
        return this.app;
    }
}
