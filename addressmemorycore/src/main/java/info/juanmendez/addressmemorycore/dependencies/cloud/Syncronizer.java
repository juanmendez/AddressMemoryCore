package info.juanmendez.addressmemorycore.dependencies.cloud;

import java.util.List;

import info.juanmendez.addressmemorycore.models.ShortAddress;
import io.reactivex.Single;

/**
 * Created by juan on 1/13/18.
 *
 */
public interface Syncronizer {
    /**
     * Concrete dependency will clear list upon logging out.
     */
    void clearLocalList();

    /**
     * Concrete dependency will refresh local database from the cloud
     */
    Single<Boolean> pullFromTheCloud();


    /**
     * Concrete dependency will push local database to the cloud
     * @return
     */
    Single<Boolean> pushToTheCloud();

    boolean isSynced();

    void setSynced( boolean synced );
}
