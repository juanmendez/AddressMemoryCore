package info.juanmendez.mapmemorycore;

import android.app.Application;

import info.juanmendez.mapmemorycore.dependencies.android.Navigation;
import info.juanmendez.mapmemorycore.dependencies.db.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AddressService;
import info.juanmendez.mapmemorycore.dependencies.network.NetworkService;
import info.juanmendez.mapmemorycore.dependencies.photo.PhotoService;

/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface CoreApp {
    Application getApplication();
    AddressProvider getAddressProvider();
    AddressService getAddressService();
    NetworkService getNeworkService();
    Navigation getNavigation();
    PhotoService getPhotoService();
}