package info.juanmendez.addressmemorycore.vp.vpAuth;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

/**
 * Created by juan on 1/8/18.
 */

public class AuthViewModel  extends BaseObservable{

    @Bindable
    public final ObservableBoolean loggedIn = new ObservableBoolean(false);
}
