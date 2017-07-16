package info.juanmendez.mapmemorycore.dependencies.autocomplete;

import info.juanmendez.mapmemorycore.models.Address;

/**
 * Created by Juan Mendez on 7/15/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AddressResponse {
    void onAddressResult(Address address );
    void onAddressError( Error error );
}
