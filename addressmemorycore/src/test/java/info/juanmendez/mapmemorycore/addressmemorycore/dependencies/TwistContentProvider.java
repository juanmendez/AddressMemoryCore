package info.juanmendez.mapmemorycore.addressmemorycore.dependencies;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.addressmemorycore.dependencies.cloud.ContentProviderService;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

/**
 * Created by juan on 1/14/18.
 */

public class TwistContentProvider {
    private ContentProviderService mService;
    private final boolean[] mSynced = {false};

    List<ShortAddress> mRemoteAddresses = new ArrayList<>();
    List<ShortAddress> mLocalAddresses = new ArrayList<>();

    public TwistContentProvider(ContentProviderService service) {
        mService = service;

        try {
            asMocked();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRemoteAddresses(List<ShortAddress> remoteAddresses) {
        mRemoteAddresses.clear();
        mRemoteAddresses.addAll( remoteAddresses );
    }

    public List<ShortAddress> getRemoteAddresses() {
        return mRemoteAddresses;
    }

    public List<ShortAddress> getLocalAddresses() {
        return mLocalAddresses;
    }

    public void setLocalAddresses(List<ShortAddress> localAddresses) {
        mLocalAddresses.clear();
        mLocalAddresses.addAll( localAddresses );
    }

    public boolean getSynced() {
        return mSynced[0];
    }

    public void setSynced(boolean synced) {
        this.mSynced[0] = synced;
    }

    private void asMocked() throws Exception {

        doAnswer(invocation -> {

            Consumer<Boolean> consumer = invocation.getArgumentAt(0, Consumer.class);

            Single<Boolean> single = Single
                                    .just( Long.valueOf(mRemoteAddresses.size() ) )
                                    .map( aLong->aLong >0 );

            single.subscribe( consumer );

            return null;
        }).when( mService ).confirmRequiresSyncing(any(Consumer.class));


        doAnswer(invocation -> {

            Consumer<Integer> consumer = invocation.getArgumentAt(0, Consumer.class);
            int updates = 0;

            if( !mService.getSynced() ) {
                for (ShortAddress address : mRemoteAddresses) {

                    /**
                     * this is a great opportunity to ensure we only add new addresses.. :)
                     */
                    if( !mLocalAddresses.contains(address)){
                        mLocalAddresses.add(address);
                        updates++;
                    }
                }
            }

            consumer.accept(updates);
            return null;
        }).when( mService ).confirmSyncing(any(Consumer.class));


        doAnswer(invocation -> {
            Boolean result = invocation.getArgumentAt(0, Boolean.class);
            mSynced[0] = result;
            return null;
        }).when( mService ).setSynced(anyBoolean());


        doAnswer(invocation -> mSynced[0]).when( mService ).getSynced();
    }

    public ContentProviderService getService() {
        return mService;
    }
}
