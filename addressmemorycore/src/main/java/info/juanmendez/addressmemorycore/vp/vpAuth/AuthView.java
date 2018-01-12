package info.juanmendez.addressmemorycore.vp.vpAuth;

import android.content.Intent;

/**
 * Created by juan on 1/8/18.
 */

public interface AuthView {
    void startActivityForResult(Intent intent, int key);
    void beforeLogin();
    void afterLogin();
}