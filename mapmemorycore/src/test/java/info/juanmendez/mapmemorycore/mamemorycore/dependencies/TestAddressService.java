package info.juanmendez.mapmemorycore.mamemorycore.dependencies;

import android.app.Application;

import info.juanmendez.mapmemorycore.dependencies.autocomplete.AddressResponse;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AddressService;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AddressesResponse;


/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class TestAddressService implements AddressService {

    Application application;

    public TestAddressService(Application application) {
        this.application = application;
    }

    @Override
    public void suggestAddress(String query, AddressesResponse response) {
    }

    @Override
    public void geolocateAddress(AddressResponse response) {

    }
}
