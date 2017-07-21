package info.juanmendez.mapmemorycore.dependencies.autocomplete;

/**
 * Created by Juan Mendez on 7/21/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface Response<T> {
    void onResult(T result );
    void onError( Exception exception );
}
