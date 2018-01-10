package info.juanmendez.addressmemorycore.vp.vpAuth;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.cloud.AuthService;
import info.juanmendez.addressmemorycore.dependencies.cloud.CloudSyncronizer;
import info.juanmendez.addressmemorycore.modules.CloudCoreModule;
import info.juanmendez.addressmemorycore.vp.Presenter;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * Created by juan on 1/8/18.
 */
public class AuthPresenter implements Presenter<AuthViewModel,AuthView> {

    private AuthView mAuthView;
    private AuthViewModel mViewModel;

    private AddressProvider mAddressProvider;
    private AuthService mAuthService;
    private CloudSyncronizer mCloudSyncronizer;
    private CompositeDisposable mComposite;

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
        mComposite = new CompositeDisposable();

        mComposite.add( mAuthService.getObservable().subscribe(loggedIn->{
            mViewModel.loggedIn.set(loggedIn);

            if( loggedIn ){
                mAddressProvider.connect();
                tryPushingToTheCloud();
            }
        }));
    }

    @Override
    public void inactive(Boolean rotated) {

        if( mAuthService.isLoggedIn() ){
            mAddressProvider.disconnect();
        }

        mComposite.dispose();
    }

    private void tryPushingToTheCloud(){

        if( !mCloudSyncronizer.isSynced() ){
            mComposite.add( mCloudSyncronizer.pushToTheCloud( mAddressProvider.getAddresses() ).subscribe(
                    aBoolean -> {
                        mCloudSyncronizer.setSynced( true );
                    },
                    throwable -> {
                        Timber.e( throwable.getMessage() );
                    }) );
        }
    }
}