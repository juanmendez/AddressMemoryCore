package info.juanmendez.addressmemorycore.dependencies;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Juan Mendez on 7/14/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface NetworkService {
    boolean isConnected();
    void connect(Consumer<Boolean> consumer);
    void disconnect();
}