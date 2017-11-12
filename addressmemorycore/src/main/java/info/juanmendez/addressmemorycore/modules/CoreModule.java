package info.juanmendez.addressmemorycore.modules;

import android.app.Application;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.AddressService;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.PhotoService;
import info.juanmendez.addressmemorycore.dependencies.WidgetService;

/**
 * Created by Juan Mendez on 11/12/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class CoreModule {

    Application mApplication;
    AddressProvider mAddressProvider;
    AddressService mAddressService;
    PhotoService mPhotoService;
    NetworkService mNetworkService;
    NavigationService mNavigationService;
    WidgetService mWidgetService;

    public Application getApplication() {
        return mApplication;
    }

    public CoreModule applyApplication(Application application) {
        mApplication = application;
        return this;
    }

    public AddressProvider getAddressProvider() {
        return mAddressProvider;
    }

    public CoreModule applyAddressProvider(AddressProvider addressProvider) {
        mAddressProvider = addressProvider;
        return this;
    }

    public AddressService getAddressService() {
        return mAddressService;
    }

    public CoreModule applyAddressService(AddressService addressService) {
        mAddressService = addressService;
        return this;
    }

    public PhotoService getPhotoService() {
        return mPhotoService;
    }

    public CoreModule applyPhotoService(PhotoService photoService) {
        mPhotoService = photoService;
        return this;
    }

    public NetworkService getNetworkService() {
        return mNetworkService;
    }

    public CoreModule applyNetworkService(NetworkService networkService) {
        mNetworkService = networkService;
        return this;
    }

    public NavigationService getNavigationService() {
        return mNavigationService;
    }

    public CoreModule applyNavigationService(NavigationService navigationService) {
        mNavigationService = navigationService;
        return this;
    }

    public WidgetService getWidgetService() {
        return mWidgetService;
    }

    public CoreModule applyWidgetService(WidgetService widgetService) {
        mWidgetService = widgetService;
        return this;
    }
}
