package info.juanmendez.mapmemorycore.vp;

import info.juanmendez.mapmemorycore.modules.DaggerMapMemoryComponent;
import info.juanmendez.mapmemorycore.modules.MapMemoryComponent;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class TestApp implements CoreApp {

    private static MapMemoryComponent component;

    public TestApp() {
        component =  DaggerMapMemoryComponent.builder().build();
    }

    public static MapMemoryComponent getComponent() {
        return component;
    }
}