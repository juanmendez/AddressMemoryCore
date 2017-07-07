package info.juanmendez.mapmemorycore.services;

import java.util.List;

import info.juanmendez.mapmemorycore.models.Address;

/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AutocompleteResponse {
    void onAddressResults(List<Address> addresses );
    void onAddressError( Error error );
}
