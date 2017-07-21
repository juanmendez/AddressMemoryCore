package info.juanmendez.mapmemorycore.dependencies.network;

import info.juanmendez.mapmemorycore.dependencies.Response;

/**
 * Created by Juan Mendez on 7/14/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface NetworkService {
    boolean isConnected();
    void connect( Response<Boolean> response );
    void disconnect();
}