package info.juanmendez.addressmemorycore.modules;

/**
 * Created by Juan Mendez on 9/12/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class MapModuleBase {
    private static MapInjector injector;

    public static MapInjector getInjector() {
        return injector;
    }

    public static void setInjector(MapInjector injector) {
        MapModuleBase.injector = injector;
    }
}
