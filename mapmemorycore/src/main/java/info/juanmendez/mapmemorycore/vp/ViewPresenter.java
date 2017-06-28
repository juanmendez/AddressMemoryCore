package info.juanmendez.mapmemorycore.vp;

/**
 * Created by Juan Mendez on 6/26/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface ViewPresenter<P,T>{

    public P onStart(T view);
    public P onPause();
}