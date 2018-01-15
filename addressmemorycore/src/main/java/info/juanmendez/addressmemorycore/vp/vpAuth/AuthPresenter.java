package info.juanmendez.addressmemorycore.vp.vpAuth;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.cloud.AuthService;
import info.juanmendez.addressmemorycore.dependencies.cloud.ContentProviderSyncronizer;
import info.juanmendez.addressmemorycore.dependencies.cloud.Syncronizer;
import info.juanmendez.addressmemorycore.modules.CloudCoreModule;
import info.juanmendez.addressmemorycore.vp.Presenter;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by juan on 1/8/18.
 */
public class AuthPresenter implements Presenter<AuthViewModel,AuthView> {
    private CompositeDisposable mComposite;

    private AuthView mAuthView;
    private AuthViewModel mViewModel;

    private AuthService mAuthService;
    private Syncronizer mSyncronizer;
    private NetworkService mNetworkService;
    private ContentProviderSyncronizer mProviderSyncronizer;

    public AuthPresenter(CloudCoreModule module) {
        mAuthService = module.getAuthService();
        mSyncronizer = module.getSyncronizer();
        mNetworkService = module.getNetworkService();
        mProviderSyncronizer = module.getContentProviderSyncronizer();
    }

    @Override
    public AuthViewModel getViewModel(AuthView view) {
        mAuthView = view;
        return mViewModel = new AuthViewModel();
    }

    @Override
    public void active(String action) {

        mComposite = new CompositeDisposable();

        /**
         * ContentProviderSyncronizer evaluates if there is a need to import
         * addresses from the free version app. If not, it still requires being called
         * as it will not do anything but will emit back to continue
         */
        mComposite.add( mProviderSyncronizer.connect().subscribe(aBoolean -> {
            loginState();
        }));
    }

    private void loginState() {
        mComposite.add( mAuthService.getObservable().subscribe(loggedIn->{
            mViewModel.loggedIn.set(loggedIn);

            if( loggedIn ){
                //lets have the provider connected while logged in
                if( mNetworkService.isConnected() ){
                    trySyncing();
                }else{
                    mAuthView.afterLogin();
                }

            }else{
                mSyncronizer.clearLocalList();
            }
        }));

        mNetworkService.connect(this::onNetworkConnection);
    }

    private void onNetworkConnection(boolean connected ){
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
    }

    @Override
    public void inactive(Boolean rotated) {

        mNetworkService.disconnect();
        mProviderSyncronizer.disconnect();
        mComposite.dispose();
    }

    private void trySyncing(){
        List<Single<Boolean>> observables = new ArrayList<>();

        //append only if user has never pushed all elements before to the cloud
        if( !mSyncronizer.isSynced() ){
            observables.add( mSyncronizer.pushToTheCloud() );
        }

        //when user logs in, we need to recover all the data
        observables.add( mSyncronizer.pullFromTheCloud() );

        Single.concat( observables).subscribe( aBoolean -> {
            mSyncronizer.setSynced( true );
            mAuthView.afterLogin();
        });
    }
}