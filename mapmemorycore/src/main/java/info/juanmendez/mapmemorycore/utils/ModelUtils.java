package info.juanmendez.mapmemorycore.utils;

import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.models.SubmitError;

/**
 * Created by Juan Mendez on 8/9/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class ModelUtils {

    public static ShortAddress cloneAddress(ShortAddress address ){

        ShortAddress cloned = new ShortAddress(address.getAddressId());

        if( !SubmitError.emptyOrNull( address.getName()) ){
            cloned.setName( address.getName() );
        }

        if( !SubmitError.emptyOrNull( address.getAddress1()) ){
            cloned.setAddress1( address.getAddress1() );
        }

        if( !SubmitError.emptyOrNull( address.getAddress2()) ){
            cloned.setAddress2( address.getAddress2() );
        }

        if( SubmitError.initialized(address.getTimesVisited()) ){
            cloned.setTimesVisited( address.getTimesVisited() );
        }

        if( address.getDateUpdated() != null ){
            cloned.setDateUpdated( address.getDateUpdated() );
        }

        if( !SubmitError.emptyOrNull( address.getMapId()) ){
            cloned.setMapId( address.getMapId() );
        }

        if( SubmitError.initialized(address.getLat()) ){
            cloned.setLat( address.getLat() );
        }

        if( SubmitError.initialized(address.getLon()) ){
            cloned.setLon( address.getLon() );
        }

        if( !SubmitError.emptyOrNull( address.getUrl()) ){
            cloned.setUrl( address.getUrl() );
        }

        if( !SubmitError.emptyOrNull( address.getPhotoLocation()) ){
            cloned.setPhotoLocation( address.getPhotoLocation() );
        }

        return cloned;
    }
}
