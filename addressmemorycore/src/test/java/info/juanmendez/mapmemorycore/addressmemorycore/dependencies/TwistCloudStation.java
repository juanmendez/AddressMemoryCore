package info.juanmendez.mapmemorycore.addressmemorycore.dependencies;

import org.mockito.Mockito;

import info.juanmendez.addressmemorycore.dependencies.cloud.AuthService;
import info.juanmendez.addressmemorycore.dependencies.cloud.CloudRef;

/**
 * Created by juan on 1/9/18.
 */

public class TwistCloudStation {
    CloudRef mCloudRef;
    AuthService mAuthService;

    public TwistCloudStation(CloudRef cloudRef, AuthService authService) {
        mCloudRef = cloudRef;
    }

    public CloudRef getCloudRef() {
        return mCloudRef;
    }

    private void mock(){
        Mockito.doAnswer(invocation -> {
            if( mAuthService.isLoggedIn() ){
                return "asdf";
            }else{
                return null;
            }

        }).when(mCloudRef).getUID();
    }
}
