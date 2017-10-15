package info.juanmendez.addressmemorycore.vp;

import android.app.Activity;

/**
 * Created by Juan Mendez on 7/15/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface FragmentNav {
    void active( String params);
    void inactive( Boolean rotated );
    int getId();
    String getTag();
    Activity getActivity();
}