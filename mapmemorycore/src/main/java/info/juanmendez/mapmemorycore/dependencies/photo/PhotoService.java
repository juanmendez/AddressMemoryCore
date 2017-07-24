package info.juanmendez.mapmemorycore.dependencies.photo;

import java.io.File;

import rx.Observable;

/**
 * Created by Juan Mendez on 7/23/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * Enables user to pick a photo
 */

public interface PhotoService {
    Observable<File> takePhoto();
    Observable<File> pickPhoto();
}