package info.juanmendez.mapmemorycore.dependencies.db;

import java.util.List;

import info.juanmendez.mapmemorycore.dependencies.Response;
import info.juanmendez.mapmemorycore.models.MapAddress;
import info.juanmendez.mapmemorycore.models.SubmitError;
import rx.Observable;

/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AddressProvider {

    //getter-setter selectedAddress
    MapAddress getSelectedAddress();
    void selectAddress(MapAddress selectedAddress);

    //get addresses
    List<MapAddress> getAddresses();
    Observable<List<MapAddress>> getAddressesAsync();

    //get address
    MapAddress getAddress(long addressId );
    Observable<MapAddress> getAddressAsync(long addressId);

    //update address
    MapAddress updateAddress(MapAddress address);
    void updateAddressAsync(MapAddress address, Response<MapAddress> response );
    void deleteAddressAsync(long addressId, Response<MapAddress> response );

    //utils
    long getNextPrimaryKey();
    long countAddresses();
    List<SubmitError> validate(MapAddress address );
}
