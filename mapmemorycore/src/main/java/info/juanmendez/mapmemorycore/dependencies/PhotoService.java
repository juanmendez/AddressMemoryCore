package info.juanmendez.mapmemorycore.dependencies;

import android.app.Activity;

import java.io.File;

import rx.Observable;

/**
 * Created by Juan Mendez on 7/23/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * Enables user to pick or take a photo. This interface is a reference of using
 * RxPaparazzo in the android project. So it returns observables of File generic type
 */

public interface PhotoService {
    Observable<File> takePhoto(Activity activity);
    Observable<File> pickPhoto(Activity activity);
}