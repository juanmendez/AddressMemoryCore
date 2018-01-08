package info.juanmendez.mapmemorycore.addressmemorycore.dependencies;

import org.mockito.Mockito;

import java.util.List;

import info.juanmendez.addressmemorycore.dependencies.cloud.CloudSyncronizer;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import io.reactivex.Observable;
import io.reactivex.Single;

import static org.powermock.api.mockito.PowerMockito.doAnswer;

/**
 * Created by juan on 1/8/18.
 */

public class TwistCloudSyncronizer {
    private CloudSyncronizer mCloudSyncronizer;
    private int mCount = 0;
    private boolean mIsSynced = false;
    private Exception mException;

    public TwistCloudSyncronizer(CloudSyncronizer cloudSyncronizer) {
        mCloudSyncronizer = cloudSyncronizer;
        setupMock();
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public void setException(Exception exception) {
        mException = exception;
    }

    private void setupMock() {
        doAnswer( invocation -> {
            return mCount;
        } ).when( mCloudSyncronizer ).countAddresses();


        doAnswer( invocation -> {
            mCount++;
            return null;
        } ).when( mCloudSyncronizer ).addOrUpdateAddress(Mockito.any(ShortAddress.class));

        doAnswer( invocation -> {
            mCount--;
            return null;
        } ).when( mCloudSyncronizer ).removeAddress(Mockito.any(ShortAddress.class));


        doAnswer( invocation -> {
            List<ShortAddress> addressList = invocation.getArgumentAt( 0, List.class );
            mCount += addressList.size();

            if( mException == null ){
                mIsSynced = true;
                return Single.just( true );
            }else{
                Single<Boolean> single = Single.create( emitter -> {
                    mIsSynced = false;
                    emitter.onError( mException );
                });

                return single;
            }

        }).when( mCloudSyncronizer ).pushToTheCloud( Mockito.anyList() );


        doAnswer( invocation -> {
           return mIsSynced;
        } ).when( mCloudSyncronizer ).isSynced();

    }
}
