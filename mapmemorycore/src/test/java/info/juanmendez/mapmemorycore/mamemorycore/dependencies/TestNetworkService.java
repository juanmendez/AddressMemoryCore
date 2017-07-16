package info.juanmendez.mapmemorycore.mamemorycore.dependencies;

import info.juanmendez.mapmemorycore.dependencies.network.NetworkResponse;
import info.juanmendez.mapmemorycore.dependencies.network.NetworkService;

/**
 * Created by Juan Mendez on 7/14/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestNetworkService implements NetworkService {

    NetworkResponse response;

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void connect( Object key, NetworkResponse response) {
        this.response = response;
    }

    @Override
    public void disconnect(Object key) {
    }
}
