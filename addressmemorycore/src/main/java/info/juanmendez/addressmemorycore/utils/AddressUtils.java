package info.juanmendez.addressmemorycore.utils;

import android.content.Intent;
import android.net.Uri;

import info.juanmendez.addressmemorycore.models.Commute;
import info.juanmendez.addressmemorycore.models.SubmitError;
import info.juanmendez.addressmemorycore.models.ShortAddress;

/**
 * Created by Juan Mendez on 8/9/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class AddressUtils {
    public static Intent fromAddress( ShortAddress address ){
        Commute commute = address.getCommute();
        Intent mapIntent;
        String uriString;

        if( commute.getType().equals(Commute.BUS)){
            uriString = String.format("https://www.google.com/maps/dir/?api=1&destination=%s %s", address.getAddress1(), address.getAddress2() );
            uriString += "&travelmode=transit";

            mapIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uriString));
        }else{
            uriString = String.format("%s %s", address.getAddress1(), address.getAddress2() );
            uriString = Uri.encode( uriString );
            uriString = String.format( "google.navigation:q=%s&mode=%s", uriString, commute.getType() );

            if( commute.getType().equals(Commute.DRIVING)){
                if( commute.getAvoidTolls() ){
                    uriString += "&avoid=t";
                }

                if( commute.getAvoidXpressway() ){
                    uriString += (commute.getAvoidTolls()?"":"&avoid=") + "h";
                }
            }

            uriString = String.format( "%s&time=%s", uriString, System.currentTimeMillis() );

            Uri gmnIntentUri = Uri.parse( uriString );

            mapIntent = new Intent( Intent.ACTION_VIEW, gmnIntentUri );
            mapIntent.setPackage( "com.google.android.apps.maps");
            mapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED|Intent.FLAG_ACTIVITY_NEW_DOCUMENT|Intent.FLAG_ACTIVITY_NEW_TASK);
        }
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

        //we need to also clone the Commute object.
        cloned.setCommute( cloneCommute( address.getCommute()) );

        return cloned;
    }

    public static Commute cloneCommute(Commute commute){
        Commute cloned = new Commute();
        cloned.setAvoidTolls(commute.getAvoidTolls());
        cloned.setType(commute.getType());
        cloned.setAvoidXpressway( commute.getAvoidXpressway());

        return cloned;
    }
}
