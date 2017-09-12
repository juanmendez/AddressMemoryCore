package info.juanmendez.mapmemorycore.modules;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@Singleton
@Component(modules={MapCoreModule.class})
public interface MapCoreComponent extends MapInjector{
}