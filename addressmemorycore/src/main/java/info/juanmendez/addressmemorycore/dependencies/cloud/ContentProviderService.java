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

    void confirmRequiresSyncing(Consumer<Boolean> consumer );
    void confirmSyncing(Consumer<Boolean> consumer);
}
