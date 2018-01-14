package info.juanmendez.addressmemorycore.vp.vpAuth;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.cloud.AuthService;
import info.juanmendez.addressmemorycore.dependencies.cloud.Syncronizer;
import info.juanmendez.addressmemorycore.modules.CloudCoreModule;
import info.juanmendez.addressmemorycore.vp.Presenter;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

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

    public AuthPresenter(CloudCoreModule module) {
        mAuthService = module.getAuthService();
        mSyncronizer = module.getSyncronizer();
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
       mNetworkService.reset();


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

                /**
                 * Lets clear up the session
                 */
                mSyncronizer.clearLocalList();
            }
        }));

        mNetworkService.connect(this::onConnect);
    }

    private void onConnect( boolean connected ){
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