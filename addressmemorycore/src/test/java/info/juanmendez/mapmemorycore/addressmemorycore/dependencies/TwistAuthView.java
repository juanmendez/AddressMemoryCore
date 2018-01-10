package info.juanmendez.mapmemorycore.addressmemorycore.dependencies;

import android.app.Activity;
import android.content.Intent;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import info.juanmendez.addressmemorycore.dependencies.cloud.AuthService;
import info.juanmendez.addressmemorycore.vp.vpAuth.AuthPresenter;
import info.juanmendez.addressmemorycore.vp.vpAuth.AuthView;

import static org.mockito.Mockito.mock;

/**
 * Created by juan on 1/9/18.
 */

public class TwistAuthView {

    private AuthView mView;
    private AuthPresenter mPresenter;
    private boolean mEnableAccess;
    private AuthService mAuthService;

    public TwistAuthView(AuthView view, AuthPresenter presenter, AuthService authService ) {
        mView = view;
        mPresenter = presenter;
        mAuthService = authService;
        setUpMock();
    }

    private void setUpMock() {
        PowerMockito.doAnswer( invocation -> {

            int key = invocation.getArgumentAt( 1, Integer.class );

            if( mEnableAccess ){
                mAuthService.onLoginResponse( key, Activity.RESULT_OK, null);
            }else{
                mAuthService.onLoginResponse( key, Activity.RESULT_CANCELED, null);
            }

            return null;
        }).when( mView ).startActivityForResult(Mockito.any(Intent.class), Mockito.anyInt() );
    }

    public boolean isEnableAccess() {
        return mEnableAccess;
    }

    public void setEnableAccess(boolean enableAccess) {
        mEnableAccess = enableAccess;
    }
}
