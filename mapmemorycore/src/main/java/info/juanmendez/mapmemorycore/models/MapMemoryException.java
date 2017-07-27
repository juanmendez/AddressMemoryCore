package info.juanmendez.mapmemorycore.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan Mendez on 7/21/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class MapMemoryException extends  Exception {

    List<SubmitError> errors = new ArrayList<>();

    //TODO: add constant error messages
    public MapMemoryException(String message) {
        super(message);
    }

    public static MapMemoryException build( String message ){
        return new MapMemoryException( message );
    }

    public List<SubmitError> getErrors() {
        return errors;
    }

    public MapMemoryException setErrors(List<SubmitError> errors) {
        this.errors = errors;

        return this;
    }
}
