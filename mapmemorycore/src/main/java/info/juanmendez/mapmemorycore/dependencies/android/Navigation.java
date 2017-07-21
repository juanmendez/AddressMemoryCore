package info.juanmendez.mapmemorycore.dependencies.android;

/**
 * Created by Juan Mendez on 7/14/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface Navigation {
    void request( String tag );
    void request( String tag, String action );
}
