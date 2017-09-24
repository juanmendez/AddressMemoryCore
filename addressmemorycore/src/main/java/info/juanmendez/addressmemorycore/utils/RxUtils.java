package info.juanmendez.addressmemorycore.utils;

import rx.Subscription;

/**
 * Created by Juan Mendez on 8/14/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class RxUtils {
    public static boolean unsubscribe(Subscription subscription ){
        if( subscription != null && !subscription.isUnsubscribed() ){
            subscription.unsubscribe();
            return true;
        }

        return false;
    }
}
