package info.juanmendez.addressmemorycore.modules;

import android.app.Application;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.AddressService;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.PhotoService;
import info.juanmendez.addressmemorycore.dependencies.WidgetService;
import info.juanmendez.addressmemorycore.models.AppConfig;

/**
 * Created by Juan Mendez on 11/12/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class AddressCoreModule {

    Application mApplication;
    AppConfig mAppConfig;
    AddressProvider mAddressProvider;
    AddressService mAddressService;
    PhotoService mPhotoService;
    NetworkService mNetworkService;
    NavigationService mNavigationService;
    WidgetService mWidgetService;

    public Application getApplication() {
        return mApplication;
    }

    public AddressCoreModule applyApplication(Application application) {
        mApplication = application;
        return this;
    }

    public AddressProvider getAddressProvider() {
        return mAddressProvider;
    }

    public AddressCoreModule applyAddressProvider(AddressProvider addressProvider) {
        mAddressProvider = addressProvider;
        return this;
    }

    public AddressService getAddressService() {
        return mAddressService;
    }

    public AddressCoreModule applyAddressService(AddressService addressService) {
        mAddressService = addressService;
        return this;
    }

    public PhotoService getPhotoService() {
        return mPhotoService;
    }

    public AddressCoreModule applyPhotoService(PhotoService photoService) {
        mPhotoService = photoService;
        return this;
    }

    public NetworkService getNetworkService() {
        return mNetworkService;
    }

    public AddressCoreModule applyNetworkService(NetworkService networkService) {
        mNetworkService = networkService;
        return this;
    }

    public NavigationService getNavigationService() {
        return mNavigationService;
    }

    public AddressCoreModule applyNavigationService(NavigationService navigationService) {
        mNavigationService = navigationService;
        return this;
    }

    public WidgetService getWidgetService() {
        return mWidgetService;
    }

    public AddressCoreModule applyWidgetService(WidgetService widgetService) {
        mWidgetService = widgetService;
        return this;
    }

    public AppConfig getAppConfig() {
        return mAppConfig;
    }

    public AddressCoreModule applyAppConfig(AppConfig appConfig) {
        mAppConfig = appConfig;
        return this;
    }
}