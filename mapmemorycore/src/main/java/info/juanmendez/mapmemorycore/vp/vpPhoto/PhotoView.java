package info.juanmendez.mapmemorycore.vp.vpPhoto;

import java.io.File;

import info.juanmendez.mapmemorycore.vp.FragmentNav;

/**
 * Created by Juan Mendez on 8/14/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface PhotoView extends FragmentNav {

    //tell fragment what is the current picture for the address
    void onPhotoSelected( File photo );
    void onPhotoError( Exception exception);
}
