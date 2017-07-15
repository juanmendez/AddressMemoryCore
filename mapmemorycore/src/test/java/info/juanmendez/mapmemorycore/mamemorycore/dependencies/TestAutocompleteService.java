package info.juanmendez.mapmemorycore.mamemorycore.dependencies;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.mapmemorycore.dependencies.autocomplete.AutocompleteResponse;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AutocompleteService;
import info.juanmendez.mapmemorycore.models.Address;


/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class TestAutocompleteService implements AutocompleteService {

    Application application;

    public TestAutocompleteService(Application application) {
        this.application = application;
    }

    @Override
    public void suggestAddress(String query, long lat, long lon, AutocompleteResponse response) {
        List<Address> addressList = new ArrayList<>();
        response.onAddressResults( addressList );
    }
}
