package info.juanmendez.mapmemorycore.dependencies.db;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import info.juanmendez.mapmemorycore.R;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AddressResponse;
import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.models.AddressFields;
import info.juanmendez.mapmemorycore.models.SubmitError;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;
import rx.Observable;

/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class DroidAddressProvider implements AddressProvider {

    Realm realm;
    Application application;
    Address selectedAddress;

    public DroidAddressProvider(Application application, Realm realm) {
        this.application = application;
        this.realm = realm;
    }

    public Address getSelectedAddress() {
        return selectedAddress;
    }

    public void selectAddress(Address selectedAddress) {
        this.selectedAddress = selectedAddress;
    }


    public List<Address> getAddresses(){
        RealmResults<Address> addresses;

        realm.beginTransaction();
            addresses = realm.where( Address.class ).findAll();
        realm.commitTransaction();

        return addresses;
    }

    public Observable<List<Address>> getAddressesAsync() {

        RealmResults<Address> addresses;

        realm.beginTransaction();
        addresses = realm.where( Address.class ).findAllAsync();
        realm.commitTransaction();

        return addresses.asObservable().map(results -> (List<Address>)results);
    }

    public Observable<Address> getAddressAsync(long addressId){

        return realm.where( Address.class )
                .equalTo( "addressId", addressId )
                .findFirstAsync().asObservable();
    }

    public Address getAddress( long addressId ){

        Address address;

        realm.beginTransaction();
        address = realm.where( Address.class ).equalTo( "addressId", addressId ).findFirst();
        realm.commitTransaction();

        return address;
    }


    public void updateAddressAsync(Address address, AddressResponse response ) {

        final Address[] addressArray = new Address[1];

        realm.executeTransactionAsync(thisRealm -> {
            addressArray[0] = thisRealm.copyToRealmOrUpdate(address);
        }, () -> {
            if (addressArray.length != 0) {
                response.onAddressResult(addressArray[0]);
            }

        }, error -> {
            response.onAddressError(new Error(error.getMessage()));
        });
    }

    public Address updateAddress(Address address){

        Address updatedAddress;

        realm.beginTransaction();
           updatedAddress = realm.copyToRealmOrUpdate( address );
        realm.commitTransaction();

        return updatedAddress;
    }

    public void deleteAddressAsync(long addressId, AddressResponse response ){

        final Address[] addressArray = new Address[1];

        realm.executeTransactionAsync(thisRealm -> {
            addressArray[0] = thisRealm.where( Address.class ).equalTo( "addressId", addressId ).findFirst();

            if( addressArray.length != 0  ){
                addressArray[0].deleteFromRealm();
            }else{
                throw new RealmException("Couldn't find an element to delete");
            }
        }, () -> {
            if( addressArray.length != 0 ){
                response.onAddressResult(addressArray[0]);
            }

        }, error -> { response.onAddressError(new Error(error.getMessage()));}  );
    }

    public long getNextPrimaryKey(){
        AtomicLong primaryKeyValue;

        try {
            primaryKeyValue = new AtomicLong(realm.where(Address.class).max("addressId").longValue());
        } catch (Exception e) {
            return 1;
        }

        return primaryKeyValue.incrementAndGet();
    }

    public long countAddresses(){
        return realm.where(Address.class).count();
    }

    public List<SubmitError> validate(Address address ){
        List<SubmitError> errors = new ArrayList<>();


        if( SubmitError.initialized( address.getAddressId() ) ){

            Address savedAddress =  getAddress( address.getAddressId() );

            //This is the only invalid field needed.
            if( savedAddress == null ){
                errors.add( new SubmitError(AddressFields.ADDRESSID, application.getString(R.string.address_gone)));
                return errors;
            }
        }

        //lets do a check for name
        if( SubmitError.emptyOrNull(address.getName()) ){
            errors.add( new SubmitError(AddressFields.NAME, application.getString(R.string.required_field)));
        }

        //if lat lon are provided we skip checking address and zipcode
        if( !SubmitError.initialized( address.getLat() ) && !SubmitError.initialized( address.getLon() ) ){

            if( SubmitError.emptyOrNull(address.getAddress1()) ){
                errors.add( new SubmitError(AddressFields.ADDRESS1, application.getString(R.string.required_field)));
            }

            if( SubmitError.emptyOrNull(address.getAddress2()) ){
                errors.add( new SubmitError(AddressFields.ADDRESS2, application.getString(R.string.required_field)));
            }
        }
        return errors;
    }
}