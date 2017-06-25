package info.juanmendez.mapmemorycore.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.juanmendez.mapmemorycore.services.AddressProvider;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

@Module
public class MapCoreModule {

    private static MapCoreComponent component;

    @Singleton
    @Provides
    public AddressProvider getAddressProvider(){
        return new AddressProvider();
    }

    public static MapCoreComponent getComponent(){

        if( component == null ){
            component = DaggerMapCoreComponent.builder().build();
        }

        return component;
    }
}
