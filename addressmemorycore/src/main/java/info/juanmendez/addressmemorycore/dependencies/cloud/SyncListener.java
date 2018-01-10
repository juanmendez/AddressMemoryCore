package info.juanmendez.addressmemorycore.dependencies.cloud;

/**
 * Created by juan on 1/2/18.
 */
public interface SyncListener<T> {

    void onChildAdded(T child);
    
    void onChildChanged(T child);
    
    void onChildRemoved(T child);
}