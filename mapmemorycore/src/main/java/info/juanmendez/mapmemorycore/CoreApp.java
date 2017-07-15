package info.juanmendez.mapmemorycore;

import android.app.Application;

import info.juanmendez.mapmemorycore.dependencies.Navigation;
import info.juanmendez.mapmemorycore.dependencies.db.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AutocompleteService;
import info.juanmendez.mapmemorycore.dependencies.network.NetworkService;

/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface CoreApp {
    Application getApplication();
    AddressProvider getAddressProvider();
    AutocompleteService getAutocomplete();
    NetworkService getNeworkService();
    Navigation getNavigation();
}