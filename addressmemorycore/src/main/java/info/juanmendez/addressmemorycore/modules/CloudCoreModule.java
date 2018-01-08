package info.juanmendez.addressmemorycore.modules;

import android.app.Application;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.cloud.AuthService;
import info.juanmendez.addressmemorycore.dependencies.cloud.CloudSyncronizer;

/**
 * Created by juan on 1/8/18.
 */

public class CloudCoreModule{

    private Application mApplication;
    private AuthService mAuthService;
    private AddressProvider mAddressProvider;
    private CloudSyncronizer mCloudSyncronizer;

    public CloudCoreModule(Application application) {
        mApplication = application;
    }

    //<editor-fold desc="authService">
    public CloudCoreModule applyAuthService(AuthService authService) {
        mAuthService = authService;
        return this;
    }

    public AuthService getAuthService() {
        return mAuthService;
    }
    //</editor-fold>

    //<editor-fold desc="addressProvider">
    public CloudCoreModule applyAddressProvider(AddressProvider addressProvider) {
        mAddressProvider = addressProvider;
        return this;
    }

    public AddressProvider getAddressProvider() {
        return mAddressProvider;
    }
    //</editor-fold>

    //<editor-fold desc="cloudSynchronizer">
    public CloudSyncronizer getCloudSyncronizer() {
        return mCloudSyncronizer;
    }

    public CloudCoreModule applyCloudSyncronizer(CloudSyncronizer cloudSyncronizer) {
        mCloudSyncronizer = cloudSyncronizer;
        return this;
    }
    //</editor-fold>
}
