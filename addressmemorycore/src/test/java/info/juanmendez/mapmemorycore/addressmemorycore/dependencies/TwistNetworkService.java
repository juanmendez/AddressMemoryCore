package info.juanmendez.mapmemorycore.addressmemorycore.dependencies;

import org.mockito.Mock;
import org.mockito.Mockito;

import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.QuickResponse;

import static org.mockito.Matchers.any;

/**
 * Created by juan on 1/12/18.
 */

public class TwistNetworkService {
    NetworkService mNetworkService;
    private boolean mIsConnected;
    private QuickResponse mQuickResponse;

    public TwistNetworkService(NetworkService networkService) {
        mNetworkService = networkService;
        asMocked();
    }


    private void asMocked(){
        Mockito.doAnswer(invocation -> {
            mQuickResponse = invocation.getArgumentAt(0, QuickResponse.class );

            //notify
            mQuickResponse.onResult( mIsConnected );
            return null;
        }).when( mNetworkService ).connect( any(QuickResponse.class));

        Mockito.doAnswer( invocation -> {
            mQuickResponse = null;
            return null;
        }).when( mNetworkService ).disconnect();

        Mockito.doAnswer( invocation -> {
            return mIsConnected;
        }).when( mNetworkService ).isConnected();
    }

    public boolean isConnected() {
        return mIsConnected;
    }

    public void setConnected(boolean connected) {
        mIsConnected = connected;

        if( mQuickResponse != null ){
            mQuickResponse.onResult( mIsConnected );
        }
    }

    public NetworkService getNetworkService() {
        return mNetworkService;
    }
}
