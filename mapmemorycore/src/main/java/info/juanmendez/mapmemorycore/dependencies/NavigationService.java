package info.juanmendez.mapmemorycore.dependencies;

import info.juanmendez.mapmemorycore.vp.FragmentNav;

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
