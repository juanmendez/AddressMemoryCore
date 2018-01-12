package info.juanmendez.addressmemorycore.dependencies.cloud;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import info.juanmendez.addressmemorycore.vp.vpAuth.AuthView;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

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
    private BehaviorSubject<Boolean> mLoginObservable;

    public AuthService(Auth auth) {
        mAuth = auth;
        mLoginObservable = BehaviorSubject.create();
    }

    public void login(@NonNull AuthView view){
        view.startActivityForResult( mAuth.getAuthIntent(), FB_SESSION );
    }

    public void logout(@NonNull AuthView view){
        mAuth.logOut(view).subscribe(aBoolean -> {
            mLoginObservable.onNext(false);
        });
    }

    public void onLoginResponse(int requestCode, int resultCode, Intent data ){

        if( requestCode == FB_SESSION ){
            mLoginObservable.onNext(resultCode == Activity.RESULT_OK );
        }
    }

    public BehaviorSubject<Boolean> getObservable() {
        return mLoginObservable;
    }

    public boolean isLoggedIn() {
        return mAuth.isLoggedIn();
    }
}