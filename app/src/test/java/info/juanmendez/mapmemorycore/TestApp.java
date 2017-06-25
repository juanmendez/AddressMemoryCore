package info.juanmendez.mapmemorycore;

import android.app.Application;

import info.juanmendez.mapmemorycore.services.AddressProvider;
import info.juanmendez.mapmemorycore.services.TestAddressProvider;

/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestApp implements CoreApp {

    @Override
    public Application getApplication() {
        return new Application();
    }

    @Override
    public AddressProvider getAddressProvider() {
        return new TestAddressProvider();
    }
}
