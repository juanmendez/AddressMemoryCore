package info.juanmendez.addressmemorycore.dependencies;

/**
 * Created by Juan Mendez on 7/14/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface NetworkService {
    boolean isConnected();
    void reset();
    void connect( QuickResponse<Boolean> response );
    void disconnect();
}