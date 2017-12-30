package info.juanmendez.addressmemorycore.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juan Mendez on 7/21/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class AddressException extends  Exception {

    private List<SubmitError> mErrors = new ArrayList<>();

    public AddressException(String message) {
        super(message);
    }

    public static AddressException build(String message ){
        return new AddressException( message );
    }

    public List<SubmitError> getErrors() {
        return mErrors;
    }

    public AddressException setErrors(List<SubmitError> errors) {
        mErrors = errors;

        return this;
    }
}
