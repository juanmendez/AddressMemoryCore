package info.juanmendez.addressmemorycore.dependencies.cloud;

import java.util.List;

import info.juanmendez.addressmemorycore.models.ShortAddress;
import io.reactivex.Single;

/**
 * Created by juan on 1/8/18.
 */

public interface CloudSyncronizer {

    void addOrUpdateAddress(ShortAddress address );

    void deleteAddress( long addressId );

    Single<Long> countAddresses();

    Single<Boolean> pushToTheCloud(List<ShortAddress> addresses);

    boolean isSynced();

    void setSynced( boolean synced );

    void connect(SyncListener<ShortAddress> syncListener);

    void disconnect();
}
