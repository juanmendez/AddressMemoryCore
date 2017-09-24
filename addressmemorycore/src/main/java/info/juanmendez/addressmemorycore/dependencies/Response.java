package info.juanmendez.addressmemorycore.dependencies;

/**
 * Created by Juan Mendez on 7/21/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface Response<T> extends ShortResponse<T> {
    void onError( Exception exception );
}