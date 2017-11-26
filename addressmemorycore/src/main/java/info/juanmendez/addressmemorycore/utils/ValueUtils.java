package info.juanmendez.addressmemorycore.utils;

/**
 * Created by Juan Mendez on 11/6/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class ValueUtils {

    public static Boolean emptyOrNull( String field ){
        return field==null || field.trim().isEmpty();
    }

    public static Boolean initialized(double field ){
        return field != 0d;
    }

    public static Boolean initialized(long field ){
        return field != 0l;
    }

    public static Boolean initialized(int field ){
        return field != 0;
    }

    public static boolean isLong( String input ) {

        if( emptyOrNull(input))
            return false;

        try {
            Long.parseLong( input );
            return true;
        }
        catch( NumberFormatException e ) {
            return false;
        }
    }
}
