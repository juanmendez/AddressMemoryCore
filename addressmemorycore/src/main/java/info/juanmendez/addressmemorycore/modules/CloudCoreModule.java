package info.juanmendez.addressmemorycore.modules;

import android.app.Application;

import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.cloud.AuthService;
import info.juanmendez.addressmemorycore.dependencies.cloud.ContentProviderService;
import info.juanmendez.addressmemorycore.dependencies.cloud.ContentProviderSyncronizer;
import info.juanmendez.addressmemorycore.dependencies.cloud.Syncronizer;

/**
 * Created by juan on 1/8/18.
 */
public class CloudCoreModule{

    private Application mApplication;
    private AuthService mAuthService;
    private Syncronizer mSyncronizer;
    private NetworkService mNetworkService;
    private ContentProviderSyncronizer mContentProviderSyncronizer;

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

    //<editor-fold desc="syncronizer">
    public Syncronizer getSyncronizer() {
        return mSyncronizer;
    }

    public CloudCoreModule applySyncronizer(Syncronizer syncronizer) {
        mSyncronizer = syncronizer;
        return this;
    }
    //</editor-fold>

    public CloudCoreModule applyNetworkService( NetworkService networkService ){
        mNetworkService = networkService;
        return this;
    }

    public NetworkService getNetworkService() {
        return mNetworkService;
    }

    public ContentProviderSyncronizer getContentProviderSyncronizer() {
        return mContentProviderSyncronizer;
    }

    public CloudCoreModule applyContentProviderSyncronizer(ContentProviderSyncronizer providerService) {
        mContentProviderSyncronizer = providerService;
        return this;
    }
}