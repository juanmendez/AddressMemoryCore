package info.juanmendez.mapmemorycore.services;

/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AutocompleteService {
    AutocompleteService setHandler( AutocompleteResponse response );
    void suggestAddress( String address, long lat, long lon );
}
