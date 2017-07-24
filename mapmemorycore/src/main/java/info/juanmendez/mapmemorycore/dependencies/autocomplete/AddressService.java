package info.juanmendez.mapmemorycore.dependencies.autocomplete;

import android.app.Activity;

import java.util.List;

import info.juanmendez.mapmemorycore.dependencies.Response;
import info.juanmendez.mapmemorycore.models.MapAddress;

/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AddressService {

    void onStart( Activity activity );
    void onStop();

    void suggestAddress( String query, Response<List<MapAddress>> response );
    void geolocateAddress( Response<MapAddress> response );
}
