package info.juanmendez.mapmemorycore.dependencies;

import android.app.Activity;

import java.util.List;

import info.juanmendez.mapmemorycore.models.ShortAddress;

/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AddressService {

    void onStart( Activity activity );
    void onStop();

    void suggestAddress( String query, Response<List<ShortAddress>> response );
    void geolocateAddress( Response<ShortAddress> response );
}
