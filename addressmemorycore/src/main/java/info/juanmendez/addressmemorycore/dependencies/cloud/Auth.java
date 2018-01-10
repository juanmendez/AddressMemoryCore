package info.juanmendez.addressmemorycore.dependencies.cloud;

import android.content.Intent;

import io.reactivex.Single;

/**
 * Created by juan on 1/9/18.
 */

public interface Auth {
    Intent getAuthIntent();
    Single<Boolean> logOut();
}
