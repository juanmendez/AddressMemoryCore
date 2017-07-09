package info.juanmendez.mapmemorycore.mamemorycore.dependencies;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AutocompleteResponse;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AutocompleteService;


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
    public void suggestAddress(AutocompleteResponse response, String address) {
        List<Address> addressList = new ArrayList<>();
        response.onAddressResults( addressList );
    }

    @Override
    public void suggestAddress(AutocompleteResponse response, long lat, long lon) {
        List<Address> addressList = new ArrayList<>();
        response.onAddressResults( addressList );
    }
}
