package info.juanmendez.addressmemorycore.dependencies.cloud;

import android.app.Activity;
import android.content.Intent;

import info.juanmendez.addressmemorycore.vp.vpAuth.AuthView;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by juan on 1/8/18.
 * This class is required to be a singleton.
 * Both the view and presenter rely on it.
 * View requires it to authenticate and retrieve result from onActivityResult
 * Presenter watches over when user has log in|out
 */
public class AuthService {

    public static final int FB_SESSION = 2018;
    private Auth mAuth;
    private AuthView mView;
    private BehaviorSubject<Boolean> mLoginObservable;

    public AuthService(Auth auth) {
        mAuth = auth;
        mLoginObservable = BehaviorSubject.create();
    }

    public void login(AuthView view){
        mView = view;
        mView.startActivityForResult( mAuth.getAuthIntent(), FB_SESSION );
    }

    public void logout(){
        if( mView != null ){
            mAuth.logOut(mView).subscribe(aBoolean -> {
                mLoginObservable.onNext(false);
            });
        }
    }

    public void onLoginResponse(int requestCode, int resultCode, Intent data ){

       mLoginObservable.onNext(requestCode == FB_SESSION && resultCode == Activity.RESULT_OK );
    }

    public BehaviorSubject<Boolean> getObservable() {
        return mLoginObservable;
    }

    public boolean isLoggedIn() {

        //even prior to login, ensure this returns false
        if( mLoginObservable.getValue() == null)
            return false;

        return mLoginObservable.getValue();
    }
}