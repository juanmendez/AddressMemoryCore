package info.juanmendez.mapmemorycore.dependencies.autocomplete;

import android.app.Activity;

/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AddressService {

    void register( Activity activity );
    void onStart();
    void onStop();

    void suggestAddress( String query, AddressesResponse response );
    void geolocateAddress( AddressResponse response );
}
