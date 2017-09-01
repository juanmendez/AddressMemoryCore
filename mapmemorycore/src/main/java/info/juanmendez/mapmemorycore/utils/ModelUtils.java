package info.juanmendez.mapmemorycore.utils;

import android.content.Intent;
import android.net.Uri;

import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.models.SubmitError;

/**
 * Created by Juan Mendez on 8/9/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class ModelUtils {
    public static Intent fromAddress( ShortAddress address, Character mode ){
        String uriString = String.format("%s %s", address.getAddress1(), address.getAddress2() );
        uriString = Uri.encode( uriString );
        uriString = String.format( "google.navigation:q=%s&mode=%s&time=%s", uriString, mode, System.currentTimeMillis() );
        Uri gmnIntentUri = Uri.parse( uriString );

        Intent mapIntent = new Intent( Intent.ACTION_VIEW, gmnIntentUri );
        mapIntent.setPackage( "com.google.android.apps.maps");

        return mapIntent;
    }

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
