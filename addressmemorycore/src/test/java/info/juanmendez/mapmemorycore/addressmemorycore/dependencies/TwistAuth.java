package info.juanmendez.mapmemorycore.addressmemorycore.dependencies;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

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

    public TwistAuth(Auth auth) {
        mAuth = auth;
        setUpMock();
    }

    private void setUpMock(){

        doAnswer(invocation -> {
            return Mockito.mock( Intent.class );
        }).when(mAuth).getAuthIntent();

        doAnswer( invocation -> {
            return Single.just(true);
        }).when( mAuth ).logOut( Mockito.any(AuthView.class));
    }

    public Auth getAuth() {
        return mAuth;
    }
}
