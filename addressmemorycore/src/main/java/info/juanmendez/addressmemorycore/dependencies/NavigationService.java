package info.juanmendez.addressmemorycore.dependencies;

import info.juanmendez.addressmemorycore.vp.FragmentNav;

/**
 * Created by Juan Mendez on 8/11/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface NavigationService {
    String getNavigationTag( FragmentNav fragmentNav );
    void request( String tag );
    boolean goBack();
}
