package info.juanmendez.mapmemorycore;

import android.app.Application;

import info.juanmendez.mapmemorycore.dependencies.RealmProvider;
import info.juanmendez.mapmemorycore.services.AutocompleteService;

/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface CoreApp {
    public Application getApplication();
    public RealmProvider getRealmProvider();
    public AutocompleteService getAutocomplete();
}