package info.juanmendez.addressmemorycore.vp.vpAuth;

import android.app.Application;

import info.juanmendez.addressmemorycore.R;
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

    private Application mApplication;
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

        /**
         * When first landing, our observer below might never emit,
         * so what we want is to find out the last state so we
         * can show the user as logged in, or out.
         * In the case of logout_action, the user wants to log out,
         * so we go about doing that request, so our observer emits it after.
         */
        if( mAuthService.isLoggedIn() ){
            if( action.equals(mAuthView.getString(R.string.logout_action))){
                //logout, and let observer emit it.
                mAuthService.logout( mAuthView );
            }else{
                //logged in, proceed to the next activity
                mAuthView.afterLogin();
            }
        }else{
            //show login menu
            mAuthView.beforeLogin();
        }

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

                //lets ensure user sees login menu
                mAuthView.beforeLogin();
            }
        }));
    }

    @Override
    public void inactive(Boolean rotated) {

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