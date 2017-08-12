package info.juanmendez.mapmemorycore.dependencies;

/**
 * Created by Juan Mendez on 7/14/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface NetworkService {
    boolean isConnected();
    void reset();
    void connect( Response<Boolean> response );
    void disconnect();
}