package info.juanmendez.mapmemorycore.dependencies.autocomplete;

/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AutocompleteService {
    /**
     * call to get list of addresses based on query
     *
     * @param query query to search for addresses returned
     * @param response handles response
     */
    void suggestAddress( String query, long lat, long lon, AutocompleteResponse response );
}
