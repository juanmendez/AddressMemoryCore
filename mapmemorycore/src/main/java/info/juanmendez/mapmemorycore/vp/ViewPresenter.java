package info.juanmendez.mapmemorycore.vp;

import info.juanmendez.mapmemorycore.dependencies.Response;
import info.juanmendez.mapmemorycore.models.MapAddress;

/**
 * Created by Juan Mendez on 6/26/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface ViewPresenter<P,T>{

    /**
     * view gets registered as soon as it is created
     * @param view
     * @return
     */
    P register(T view);

    /**
     * we don't need to keep in mind what is onStart and onPause
     * instead we use active/inactive
     * @param action
     */
    void active( String action);
    void inactive();

    void submitAddress( Response<MapAddress> response );
}