package info.juanmendez.addressmemorycore.dependencies.cloud;

import java.util.List;

import info.juanmendez.addressmemorycore.models.ShortAddress;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by juan on 1/8/18.
 */

public interface CloudSyncronizer {

    void addOrUpdateAddress(ShortAddress address );
    void removeAddress( ShortAddress address );
    int countAddresses();

    Single<Boolean> pushToTheCloud(List<ShortAddress> addresses);
    boolean isSynced();
}
