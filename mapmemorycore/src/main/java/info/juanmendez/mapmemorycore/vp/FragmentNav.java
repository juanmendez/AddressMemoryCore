package info.juanmendez.mapmemorycore.vp;

import android.app.Activity;

/**
 * Created by Juan Mendez on 7/15/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface FragmentNav {
    void active( String action);
    void inactive( Boolean rotated );
    int getId();
    String getTag();
    Activity getActivity();
}