package info.juanmendez.mapmemorycore.addressmemorycore.dependencies;

import info.juanmendez.addressmemorycore.dependencies.cloud.AuthService;

import static org.powermock.api.mockito.PowerMockito.doAnswer;

/**
 * Created by juan on 1/8/18.
 */

public class TwistAuthService {
    private AuthService mAuthService;
    private final boolean[] mIsLoggedIn = {false};


    public TwistAuthService(AuthService authService) {
        mAuthService = authService;
        setUpMock();
    }

    public void setLoggedIn( boolean isLoggedIn ){
        mIsLoggedIn[0] = isLoggedIn;
    }

    private void setUpMock(){
        doAnswer(invocation -> {
            return mIsLoggedIn[0];
        }).when(mAuthService).isLoggedIn();

        doAnswer(invocation -> {
            mIsLoggedIn[0] = true;
            return null;
        }).when(mAuthService).login();

        doAnswer(invocation -> {
            mIsLoggedIn[0] = false;
            return null;
        }).when(mAuthService).logout();
    }
}
