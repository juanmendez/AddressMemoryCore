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
        return copyAddress( address, new ShortAddress());
    }

    public static ShortAddress copyAddress(ShortAddress originalAddress, ShortAddress copiedAddress ){

        copiedAddress.setAddressId( originalAddress.getAddressId() );

        if( !SubmitError.emptyOrNull( originalAddress.getName()) ){
            copiedAddress.setName( originalAddress.getName() );
        }

        if( !SubmitError.emptyOrNull( originalAddress.getAddress1()) ){
            copiedAddress.setAddress1( originalAddress.getAddress1() );
        }

        if( !SubmitError.emptyOrNull( originalAddress.getAddress2()) ){
            copiedAddress.setAddress2( originalAddress.getAddress2() );
        }

        if( SubmitError.initialized(originalAddress.getTimesVisited()) ){
            copiedAddress.setTimesVisited( originalAddress.getTimesVisited() );
        }

        if( originalAddress.getDateUpdated() != null ){
            copiedAddress.setDateUpdated( originalAddress.getDateUpdated() );
        }

        if( !SubmitError.emptyOrNull( originalAddress.getMapId()) ){
            copiedAddress.setMapId( originalAddress.getMapId() );
        }

        if( SubmitError.initialized(originalAddress.getLat()) ){
            copiedAddress.setLat( originalAddress.getLat() );
        }

        if( SubmitError.initialized(originalAddress.getLon()) ){
            copiedAddress.setLon( originalAddress.getLon() );
        }

        if( !SubmitError.emptyOrNull( originalAddress.getUrl()) ){
            copiedAddress.setUrl( originalAddress.getUrl() );
        }

        if( !SubmitError.emptyOrNull( originalAddress.getPhotoLocation()) ){
            copiedAddress.setPhotoLocation( originalAddress.getPhotoLocation() );
        }

        //we need to also clone the Commute object.
        copiedAddress.setCommute( cloneCommute( originalAddress.getCommute()) );

        return copiedAddress;
    }

    public static Commute cloneCommute(Commute commute){
        Commute cloned = new Commute();
        cloned.setAvoidTolls(commute.getAvoidTolls());
        cloned.setType(commute.getType());
        cloned.setAvoidXpressway( commute.getAvoidXpressway());

        return cloned;
    }
}
