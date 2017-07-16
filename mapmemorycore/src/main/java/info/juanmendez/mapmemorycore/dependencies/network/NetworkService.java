package info.juanmendez.mapmemorycore.dependencies.network;

/**
 * Created by Juan Mendez on 7/14/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface NetworkService {
    boolean isConnected();
    void connect( Object key, NetworkResponse response );
    void disconnect( Object key );
}