package info.juanmendez.mapmemorycore;

import android.app.Application;

import info.juanmendez.mapmemorycore.dependencies.AddressService;
import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.NavigationService;
import info.juanmendez.mapmemorycore.dependencies.NetworkService;
import info.juanmendez.mapmemorycore.dependencies.PhotoService;
import info.juanmendez.mapmemorycore.dependencies.WidgetService;

/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface CoreApp {
    Application getApplication();
    AddressProvider getAddressProvider();
    AddressService getAddressService();
    NetworkService getNetworkService();
    PhotoService getPhotoService();
    NavigationService getNavigationService();
    WidgetService getWidgetService();
}