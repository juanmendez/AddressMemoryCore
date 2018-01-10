package info.juanmendez.mapmemorycore.addressmemorycore.dependencies;

import org.mockito.Mockito;

import info.juanmendez.addressmemorycore.dependencies.cloud.AuthService;
import info.juanmendez.addressmemorycore.dependencies.cloud.CloudStation;

/**
 * Created by juan on 1/9/18.
 */

public class TwistCloudStation {
    CloudStation mCloudStation;
    AuthService mAuthService;

    public TwistCloudStation(CloudStation cloudStation, AuthService authService) {
        mCloudStation = cloudStation;
    }

    public CloudStation getCloudStation() {
        return mCloudStation;
    }

    private void mock(){
        Mockito.doAnswer(invocation -> {
            if( mAuthService.isLoggedIn() ){
                return "asdf";
            }else{
                return null;
            }

        }).when( mCloudStation ).getUID();
    }
}
