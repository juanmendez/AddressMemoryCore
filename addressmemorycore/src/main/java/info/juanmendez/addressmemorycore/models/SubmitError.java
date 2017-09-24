package info.juanmendez.addressmemorycore.models;

/**
 * Created by Juan Mendez on 7/5/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class SubmitError{
    String field;
    String message;

    public SubmitError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

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
}