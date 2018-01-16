package info.juanmendez.addressmemorycore.dependencies.cloud;

import java.util.List;

import info.juanmendez.addressmemorycore.models.ShortAddress;
import io.reactivex.functions.Consumer;

/**
 * Created by juan on 1/14/18.
 */

public interface ContentProviderService {

    void setSynced( boolean synced );
    boolean getSynced();

    void connect();
    void disconnect();

    //subscribe and be notified if there is syncing required
    void confirmRequiresSyncing(Consumer<Boolean> consumer ) throws Exception;

    //subscribe and be notified about number of records added
    void confirmSyncing(Consumer<Integer> consumer) throws Exception;
}
