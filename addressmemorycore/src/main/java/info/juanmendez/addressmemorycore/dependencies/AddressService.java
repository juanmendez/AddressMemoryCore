package info.juanmendez.addressmemorycore.dependencies;

import android.app.Activity;
import android.location.Location;

import java.util.List;

import info.juanmendez.addressmemorycore.models.ShortAddress;

/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AddressService {

    void onStart(Activity activity, QuickResponse<Boolean> connectionResponse );
    void onStop();

    void suggestAddress( String query, Response<List<ShortAddress>> response );
    void geolocateAddress( Response<ShortAddress> response );

    boolean isConnected();
}
