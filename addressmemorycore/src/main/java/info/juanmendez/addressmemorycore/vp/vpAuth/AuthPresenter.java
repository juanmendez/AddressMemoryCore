package info.juanmendez.addressmemorycore.vp.vpAuth;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.cloud.AuthService;
import info.juanmendez.addressmemorycore.dependencies.cloud.CloudSyncronizer;
import info.juanmendez.addressmemorycore.modules.CloudCoreModule;
import info.juanmendez.addressmemorycore.vp.Presenter;
import io.reactivex.disposables.Disposable;

import static junit.framework.Assert.assertTrue;

/**
 * Created by juan on 1/8/18.
 */

public class AuthPresenter implements Presenter<AuthViewModel,AuthView> {

    private AuthView mAuthView;
    private AuthViewModel mViewModel;

    private AddressProvider mAddressProvider;
    private AuthService mAuthService;
    private CloudSyncronizer mCloudSyncronizer;
    private Disposable mPushDisposable;

    public AuthPresenter(CloudCoreModule module) {
        mAddressProvider = module.getAddressProvider();
        mAuthService = module.getAuthService();
        mCloudSyncronizer = module.getCloudSyncronizer();
    }

    @Override
    public AuthViewModel getViewModel(AuthView view) {
        mAuthView = view;
        return mViewModel = new AuthViewModel();
    }

    @Override
    public void active(String action) {

    }

    @Override
    public void inactive(Boolean rotated) {

    }

    public void login(){
        mAuthService.login();
        tryPushingToTheCloud();
    }

    public void logout(){
        mAuthService.logout();
    }

    private void tryPushingToTheCloud(){

        if( !mCloudSyncronizer.isSynced() ){
            mPushDisposable = mCloudSyncronizer.pushToTheCloud( mAddressProvider.getAddresses() ).subscribe(
                    aBoolean -> {
                    },
                    throwable -> {

                    });
        }
    }
}
