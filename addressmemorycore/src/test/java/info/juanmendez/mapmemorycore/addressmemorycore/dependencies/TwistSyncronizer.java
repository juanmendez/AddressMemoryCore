package info.juanmendez.mapmemorycore.addressmemorycore.dependencies;

import org.mockito.Mockito;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.cloud.Syncronizer;
import io.reactivex.Single;

import static org.powermock.api.mockito.PowerMockito.doAnswer;

/**
 * Created by juan on 1/8/18.
 */

public class TwistSyncronizer {
    private Syncronizer mSyncronizer;
    private AddressProvider mAddressProvider;

    private boolean mIsSynced = false;
    private Exception mException;

    public TwistSyncronizer(Syncronizer syncronizer, AddressProvider addressProvider) {
        mSyncronizer = syncronizer;
        mAddressProvider = addressProvider;
        setupMock();
    }

    public boolean isSynced() {
        return mIsSynced;
    }

    public void setSynced(boolean synced) {
        mIsSynced = synced;
    }

    public void setException(Exception exception) {
        mException = exception;
    }

    private void setupMock() {

        doAnswer( invocation -> {

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

        }).when(mSyncronizer).pushToTheCloud();


        doAnswer( invocation -> {

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

        }).when(mSyncronizer).pullFromTheCloud();


        doAnswer( invocation -> {
           return mIsSynced;
        } ).when(mSyncronizer).isSynced();

        doAnswer( invocation -> {
            mIsSynced = invocation.getArgumentAt(0, boolean.class);
            return null;
        }).when(mSyncronizer).setSynced( Mockito.anyBoolean() );


        doAnswer( invocation -> {
            mAddressProvider.deleteAddresses();
            return null;
        }).when(mSyncronizer).clearLocalList();
    }
}