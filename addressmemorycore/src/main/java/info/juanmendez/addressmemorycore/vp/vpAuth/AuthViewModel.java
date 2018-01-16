package info.juanmendez.addressmemorycore.vp.vpAuth;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

/**
 * Created by juan on 1/8/18.
 */

public class AuthViewModel  extends BaseObservable{

    public static final int NO_SYNC_DISPLAY = 0; //user doesn't see sync notification
    public static final int SYNC_NOTIFICATION = 1; //user sees notification
    public static final int SYNC_CONFIRMED = 2; //user accepts it

    @Bindable
    public final ObservableBoolean loggedIn = new ObservableBoolean(false);

    @Bindable
    public final ObservableBoolean loginWhenOnline = new ObservableBoolean( false );


    @Bindable
    public final ObservableInt notifySyncing = new ObservableInt( NO_SYNC_DISPLAY );
}
