package info.juanmendez.addressmemorycore.utils;

import io.reactivex.disposables.Disposable;

/**
 * Created by Juan Mendez on 8/14/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class RxUtils {

    //TODO: rename dispose
    public static boolean unsubscribe(Disposable disposable ){
        if( disposable != null && !disposable.isDisposed() ){
            disposable.dispose();
            return true;
        }

        return false;
    }
}
