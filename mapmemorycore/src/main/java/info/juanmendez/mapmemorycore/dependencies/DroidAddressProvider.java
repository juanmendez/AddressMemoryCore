package info.juanmendez.mapmemorycore.dependencies;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import info.juanmendez.mapmemorycore.R;
import info.juanmendez.mapmemorycore.models.AddressFields;
import info.juanmendez.mapmemorycore.models.MapMemoryException;
import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.models.SubmitError;
import info.juanmendez.mapmemorycore.utils.ModelUtils;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import rx.Observable;

/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class DroidAddressProvider implements AddressProvider {

    private Realm realm;
    private Application application;
    private ShortAddress selectedAddress;

    public DroidAddressProvider(Application application, Realm realm) {
        this.application = application;
        this.realm = realm;
    }

    public ShortAddress getSelectedAddress() {
        return selectedAddress;
    }

    public void selectAddress(ShortAddress selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public List<ShortAddress> getAddresses(){
        RealmResults<ShortAddress> addresses;

        realm.beginTransaction();
            addresses = realm.where( ShortAddress.class ).findAll();
        realm.commitTransaction();

        return addresses;
    }

    public Observable<List<ShortAddress>> getAddressesAsync() {

        RealmResults<ShortAddress> addresses;

        realm.beginTransaction();
        addresses = realm.where( ShortAddress.class ).findAllAsync();
        realm.commitTransaction();

        return addresses.asObservable().map(results -> (List<ShortAddress>)results);
    }

    public Observable<ShortAddress> getAddressAsync(long addressId){

        return realm.where( ShortAddress.class )
                .equalTo( AddressFields.ADDRESSID, addressId )
                .findFirstAsync().asObservable();
    }

    public ShortAddress getAddress(long addressId ){

        ShortAddress address;

        realm.beginTransaction();
        address = realm.where( ShortAddress.class ).equalTo( AddressFields.ADDRESSID, addressId ).findFirst();
        realm.commitTransaction();

        return address;
    }

    /**
     * Updates or adds a new address.
     * The created or updated address gets cloned and assigned to response.onResult()
     * @param address initial address provided to update or create
     * @param response handles back the result from realm
     */
    public void updateAddressAsync(ShortAddress address, Response<ShortAddress> response ) {

        //assign id if it's a new address
        if( !SubmitError.initialized(address.getAddressId()) ){
            address.setAddressId( getNextPrimaryKey() );
        }

        realm.executeTransactionAsync(thisRealm -> thisRealm.copyToRealmOrUpdate(address), () -> {
            ShortAddress addressResult = getAddress( address.getAddressId() );
            response.onResult( ModelUtils.cloneAddress( addressResult ) );
        }, exception -> response.onError(new MapMemoryException(exception.getMessage())));
    }

    /**
     * Similar to the previous method, it returns a created updated address
     * in synchronous mode.
     * @param address
     * @return  address is a cloned copy
     */
    public ShortAddress updateAddress(ShortAddress address){

        //assign id if it's a new address
        if( address.getAddressId() == 0 ){
            address.setAddressId( getNextPrimaryKey() );
        }

        ShortAddress updatedAddress;

        realm.beginTransaction();
           updatedAddress = realm.copyToRealmOrUpdate( address );
        realm.commitTransaction();

        return ModelUtils.cloneAddress( updatedAddress );
    }

    /**
     * deletes in asynchronous mode the address, and returns back the address found
     * prior to being deleted
     * @param addressId
     * @param response
     */
    public void deleteAddressAsync(long addressId, Response<Boolean> response ){

        realm.executeTransactionAsync(thisRealm -> {
            ShortAddress deletedAddress = thisRealm.where( ShortAddress.class ).equalTo( AddressFields.ADDRESSID, addressId ).findFirst();
            deletedAddress.deleteFromRealm();
        },
        () -> response.onResult( true ),
        exception -> response.onError(new MapMemoryException(application.getString(R.string.address_gone))));
    }

    /**
     * checks on getting the next addressId for address
     * @return
     */
    public long getNextPrimaryKey(){
        AtomicLong primaryKeyValue;

        try {
            primaryKeyValue = new AtomicLong(realm.where(ShortAddress.class).max(AddressFields.ADDRESSID).longValue());
        } catch (Exception e) {
            return 1;
        }

        return primaryKeyValue.incrementAndGet();
    }

    public long countAddresses(){
        return realm.where(ShortAddress.class).count();
    }

    public List<SubmitError> validate(ShortAddress address ){
        List<SubmitError> errors = new ArrayList<>();


        if( SubmitError.initialized( address.getAddressId() ) ){

            ShortAddress savedAddress =  getAddress( address.getAddressId() );

            //This is the only invalid field needed.
            if( savedAddress == null ){
                errors.add( new SubmitError(AddressFields.ADDRESSID, application.getString(R.string.address_gone)));
                return errors;
            }
        }

        //name is required, but if provided we need to ensure it is unique as well.
        if( SubmitError.emptyOrNull(address.getName()) ){
            errors.add( new SubmitError(AddressFields.NAME, application.getString(R.string.required_field)));
        }else{
            RealmResults<ShortAddress> addresses;

            realm.beginTransaction();
                RealmQuery<ShortAddress> addressRealmQuery = realm.where(ShortAddress.class).equalTo( AddressFields.NAME, address.getName(), Case.INSENSITIVE );

                //if it's an update, the name must not match other siblings
                if( SubmitError.initialized( address.getAddressId() )){
                    addressRealmQuery.notEqualTo( AddressFields.ADDRESSID, address.getAddressId() );
                }

                addresses = addressRealmQuery.findAll();
            realm.commitTransaction();


            if( !addresses.isEmpty() ){
                errors.add( new SubmitError( AddressFields.NAME, application.getString(R.string.address_name_repeated)));
            }
        }

        //if lat lon are provided we skip checking address and zipcode
        if( !SubmitError.initialized( address.getLat() ) && !SubmitError.initialized( address.getLon() ) ){

            if( SubmitError.emptyOrNull(address.getAddress1()) ){
                errors.add( new SubmitError(AddressFields.ADDRESS1, application.getString(R.string.required_field)));
            }
        }
        return errors;
    }
}