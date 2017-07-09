package info.juanmendez.mapmemorycore.dependencies.autocomplete;

/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AutocompleteService {
    /**
     * call to get list of addresses based on query
     * @param response handles response
     * @param address query to search for addresses returned
     */
    void suggestAddress( AutocompleteResponse response, String address );


    /**
     * call to get list of addresses based on geolocation
     * @param response handles response
     * @param lat/lon geolocation to query for addresses
     */
    void suggestAddress( AutocompleteResponse response, long lat, long lon );
}
