package info.juanmendez.mapmemorycore.vp;

import android.app.Activity;

/**
 * Created by Juan Mendez on 7/15/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface FragmentNav {
    void setActive( Boolean active, String action);
    Activity getActivity();
}