package info.juanmendez.mapmemorycore.vp.vpAddress;

import java.util.List;

import javax.inject.Inject;

import info.juanmendez.mapmemorycore.dependencies.Response;
import info.juanmendez.mapmemorycore.dependencies.android.Navigation;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AddressService;
import info.juanmendez.mapmemorycore.dependencies.db.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.network.NetworkService;
import info.juanmendez.mapmemorycore.dependencies.photo.PhotoService;
import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.models.MapMemoryException;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.vp.ViewPresenter;
import rx.Subscription;

/**
 * Created by Juan Mendez on 6/26/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class AddressPresenter implements ViewPresenter<AddressPresenter,AddressFragment>{

    @Inject
    AddressProvider addressProvider;

    @Inject
    AddressService addressService;

    @Inject
    NetworkService networkService;

    @Inject
    PhotoService photoService;

    @Inject
    Navigation navigation;

    AddressFragment view;
    Address addressEdited;


    public static final String ADDRESS_VIEW_TAG = "viewAddressTag";
    public static final String ADDDRESS_EDIT_TAG = "editAddressTag";
    private Subscription fileSubscription;

    @Override
    public AddressPresenter register(AddressFragment view) {
        this.view = view;
        MapCoreModule.getComponent().inject(this);
        addressService.register( view.getActivity() );
        return this;
    }

    @Override
    public void active( String action ) {

        networkService.reset();
        networkService.connect(new Response<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                view.onAddressResult( new Address(0), result );
            }

            @Override
            public void onError(Exception exception) {

            }
        });

        addressService.onStart();
    }

    @Override
    public void inactive() {
        networkService.disconnect();
        addressService.onStop();
        cancelPhotoSubscription();
    }

    /**
     * View is requesting to pull address based on geolocation
     * this is done in an asynchronous way
     */
    public void requestAddressByGeolocation(){
        if( networkService.isConnected() ){
            addressService.geolocateAddress(new Response<Address>() {
                @Override
                public void onResult(Address result) {
                    view.onAddressResult( result, networkService.isConnected() );
                }

                @Override
                public void onError(Exception exception) {
                    view.onAddressError(exception);
                }
            });
        }else{
            view.onAddressError( new MapMemoryException("networkService has no connection"));
        }
    }

    /**
     * View is requesting addresses by query which is replied asynchronously
     */
    public void requestAddressSuggestions( String query ){
        if( networkService.isConnected() && !query.isEmpty() ){
            addressService.suggestAddress(query, new Response<List<Address>>() {
                @Override
                public void onResult(List<Address> results ) {
                    view.onAddressesSuggested( results );
                }

                @Override
                public void onError(Exception exception) {
                    view.onAddressError( exception );
                }
            });
        }else{
            if( !networkService.isConnected() )
                view.onAddressError( new MapMemoryException("networkService has no connection"));
            else if( query.isEmpty() )
                view.onAddressError( new MapMemoryException("query is empty"));
        }
    }

    //view requests to pick photo from public gallery
    public void requestPickPhoto(){
        fileSubscription = photoService.pickPhoto().subscribe(file -> {
            view.onPhotoSelected( file );
        }, throwable -> {

        }, () -> {
            cancelPhotoSubscription();
        });
    }

    //view requests to take a photo
    public void requestTakePhoto(){
        fileSubscription = photoService.takePhoto().subscribe(file -> {
            view.onPhotoSelected( file );
        }, throwable -> {

        }, () -> {
            cancelPhotoSubscription();
        });
    }

    private void cancelPhotoSubscription(){
        if( fileSubscription != null && !fileSubscription.isUnsubscribed() ){
            fileSubscription.unsubscribe();
            fileSubscription = null;
        }
    }
}