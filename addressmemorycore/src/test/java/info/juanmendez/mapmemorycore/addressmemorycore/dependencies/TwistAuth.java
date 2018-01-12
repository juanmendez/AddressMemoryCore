package info.juanmendez.mapmemorycore.addressmemorycore.dependencies;

import android.content.Intent;

import org.mockito.Mockito;

import info.juanmendez.addressmemorycore.dependencies.cloud.Auth;
import info.juanmendez.addressmemorycore.dependencies.cloud.AuthService;
import info.juanmendez.addressmemorycore.vp.vpAuth.AuthView;
import io.reactivex.Single;

import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by juan on 1/8/18.
 */

public class TwistAuth {

    Auth mAuth;
    AuthService mAuthService;
    boolean mIsLoggedIn = false;

    public TwistAuth(Auth auth, AuthService authService) {
        mAuth = auth;
        mAuthService = authService;
        setUpMock();
    }

    private void setUpMock(){

        doAnswer(invocation -> {
            return Mockito.mock( Intent.class );
        }).when(mAuth).getAuthIntent();

        doAnswer( invocation -> {
            mIsLoggedIn = false;
            return Single.just(true);
        }).when( mAuth ).logOut( Mockito.any(AuthView.class));

        doAnswer(invocation -> {
            return mIsLoggedIn;
        }).when(mAuth).isLoggedIn();
    }

    public Auth getAuth() {
        return mAuth;
    }

    public void login( AuthView authView ){
        mIsLoggedIn = true;
        mAuthService.login( authView );
    }

    public void logout( AuthView authView ){
        mIsLoggedIn = false;
        mAuthService.logout( authView );
    }

    public boolean isLoggedIn() {
        return mIsLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        mIsLoggedIn = loggedIn;
    }
}
