package info.juanmendez.addressmemorycore.vp.vpAuth;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import info.juanmendez.addressmemorycore.R;
import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.WidgetService;
import info.juanmendez.addressmemorycore.dependencies.cloud.AuthService;
import info.juanmendez.addressmemorycore.dependencies.cloud.CloudSyncronizer;
import info.juanmendez.addressmemorycore.modules.CloudCoreModule;
import info.juanmendez.addressmemorycore.vp.Presenter;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * Created by juan on 1/8/18.
 */
public class AuthPresenter implements Presenter<AuthViewModel,AuthView> {

    private AuthView mAuthView;
    private AuthViewModel mViewModel;

    private Application mApplication;
    private AddressProvider mAddressProvider;
    private AuthService mAuthService;
    private CloudSyncronizer mCloudSyncronizer;
    private CompositeDisposable mComposite;
    private NetworkService mNetworkService;

    public AuthPresenter(CloudCoreModule module) {
        mAddressProvider = module.getAddressProvider();
        mAuthService = module.getAuthService();
        mCloudSyncronizer = module.getCloudSyncronizer();
        mNetworkService = module.getNetworkService();
    }

    @Override
    public AuthViewModel getViewModel(AuthView view) {
        mAuthView = view;
        return mViewModel = new AuthViewModel();
    }

    @Override
    public void active(String action) {

       mComposite = new CompositeDisposable();


        mComposite.add( mAuthService.getObservable().subscribe(loggedIn->{
            mViewModel.loggedIn.set(loggedIn);

            if( loggedIn ){
                //lets have the provider connected while logged in
                tryPushingToTheCloud();
            }else{

                /**
                 * This is something new, we want to clear addresses while logged out.
                 * When working with a cloud based addressProvider, then this will just
                 * affect the realm based provider.
                 */
                mAddressProvider.connect();
                mAddressProvider.deleteAddresses();
                mAddressProvider.disconnect();
            }
        }));

        mNetworkService.reset();
        mNetworkService.connect(connected -> {
            if( !mAuthService.isLoggedIn() ){
                if (connected) {
                    mViewModel.loginWhenOnline.set(false);
                    mAuthView.beforeLogin();
                }else{
                    mViewModel.loginWhenOnline.set(true);
                }
            }else{
                mViewModel.loginWhenOnline.set(false);
            }
        });
    }

    @Override
    public void inactive(Boolean rotated) {

        mNetworkService.disconnect();
        mComposite.dispose();
    }

    private void tryPushingToTheCloud(){

        if( !mCloudSyncronizer.isSynced() ){
            mAddressProvider.connect();
            mComposite.add( mCloudSyncronizer.pushToTheCloud( mAddressProvider.getAddresses() ).subscribe(
                    aBoolean -> {
                        mCloudSyncronizer.setSynced( true );
                        mAddressProvider.disconnect();
                        mAuthView.afterLogin();
                    },
                    throwable -> {
                        Timber.e( throwable.getMessage() );
                        mAddressProvider.disconnect();
                    } ) );
        }else{
            mAuthView.afterLogin();
        }
    }
}