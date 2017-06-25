package info.juanmendez.mapmemorycore;

import android.content.Context;

import info.juanmendez.mapmemorycore.dependencies.RealmProvider;

/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface CoreApp {
    public Context getApplication();
    public RealmProvider getRealmProvider();
}