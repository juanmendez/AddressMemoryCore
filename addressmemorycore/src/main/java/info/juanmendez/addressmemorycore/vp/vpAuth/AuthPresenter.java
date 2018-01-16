package info.juanmendez.addressmemorycore.vp.vpAuth;

import android.databinding.Observable;
import android.databinding.Observable.OnPropertyChangedCallback;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.cloud.AuthService;
import info.juanmendez.addressmemorycore.dependencies.cloud.ContentProviderSyncronizer;
import info.juanmendez.addressmemorycore.dependencies.cloud.Syncronizer;
import info.juanmendez.addressmemorycore.modules.CloudCoreModule;
import info.juanmendez.addressmemorycore.vp.Presenter;
import io.reactivex.Completable;
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
    private OnPropertyChangedCallback mCallBack;

    public AuthPresenter(CloudCoreModule module) {
        mAuthService = module.getAuthService();
        mSyncronizer = module.getSyncronizer();
        mNetworkService = module.getNetworkService();
        mProviderSyncronizer = module.getContentProviderSyncronizer();
        mComposite = new CompositeDisposable();
    }

    @Override
    public AuthViewModel getViewModel(AuthView view) {
        mAuthView = view;
        return mViewModel = new AuthViewModel();
    }

    @Override
    public void active(String action) {

        /**
         * ContentProviderSyncronizer evaluates if there is a need to import
         * addresses from the free version app. If not, it still requires being called
         * as it will not do anything but will emit back to continue
         */
        mComposite.add( mProviderSyncronizer.connect().subscribe((Boolean synced) -> {

            if( synced ){
                /**
                 * Let the user know we have synced his free app into the paid app.
                 */
                mViewModel.notifySyncing.set(AuthViewModel.SYNC_NOTIFICATION);

                /**
                 * User will confirm she read the message, and then continue through the login process
                 */
                mViewModel.notifySyncing.addOnPropertyChangedCallback( mCallBack = new OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable sender, int brID) {
                        if( mViewModel.notifySyncing.get() == AuthViewModel.SYNC_CONFIRMED){
                            loginState();
                        }
                    }
                });
            }else{
                loginState();
            }
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

            }else if(mSyncronizer.isSynced()){
                //clear only if already synced
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
        mComposite.clear();

        if( mCallBack != null ){
            mViewModel.removeOnPropertyChangedCallback( mCallBack );
            mCallBack = null;
        }
    }

    private void trySyncing(){
        List<Completable> observables = new ArrayList<>();

        //append only if user has never pushed all elements before to the cloud
        if( !mSyncronizer.isSynced() ){
            observables.add( Completable.fromSingle(mSyncronizer.pushToTheCloud()) );
        }

        //when user logs in, we need to recover all the data
        observables.add( Completable.fromSingle(mSyncronizer.pullFromTheCloud()) );

        Completable.concat( observables).subscribe(() -> {
            mSyncronizer.setSynced( true );
            mAuthView.afterLogin();
        });
    }
}