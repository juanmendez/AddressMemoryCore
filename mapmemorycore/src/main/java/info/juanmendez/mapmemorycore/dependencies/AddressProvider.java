package info.juanmendez.mapmemorycore.dependencies;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;
import javax.inject.Singleton;

import info.juanmendez.mapmemorycore.R;
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

@Singleton
public class AddressProvider {

    Realm realm;
    ResourcesProvider resourcesProvider;
    Address selectedAddress;

    @Inject
    public AddressProvider(ResourcesProvider resourcesProvider, RealmProvider realmProvider) {
        this.resourcesProvider = resourcesProvider;
        this.realm = realmProvider.getRealm();
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public Address getSelectedAddress() {
        return selectedAddress;
    }

    public void selectAddress(Address selectedAddress) {
        this.selectedAddress = selectedAddress;
    }


    public RealmResults<Address> getAddresses(){
        RealmResults<Address> addresses;

        realm.beginTransaction();
            addresses = realm.where( Address.class ).findAll();
        realm.commitTransaction();

        return addresses;
    }

    public Observable<RealmResults<Address>> getAddressesAsync() {

        RealmResults<Address> addresses;

        realm.beginTransaction();
        addresses = realm.where( Address.class ).findAllAsync();
        realm.commitTransaction();

        return addresses.asObservable();
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


    public void updateAddressAsync(Address address, Realm.Transaction.OnSuccess successHandler, Realm.Transaction.OnError errorHandler ){

        realm.executeTransactionAsync(thisRealm -> {
            thisRealm.copyToRealmOrUpdate( address );
        }, successHandler, errorHandler );
    }

    public Address updateAddress(Address address){

        Address updatedAddress;

        realm.beginTransaction();
           updatedAddress = realm.copyToRealmOrUpdate( address );
        realm.commitTransaction();

        return updatedAddress;
    }

    public void deleteAddressAsync(long addressId, Realm.Transaction.OnSuccess successHandler, Realm.Transaction.OnError errorHandler ){
        realm.executeTransactionAsync(thisRealm -> {
            RealmResults<Address> addresses = thisRealm.where( Address.class ).equalTo( "addressId", addressId ).findAll();

            if( !addresses.isEmpty() ){
                addresses.deleteAllFromRealm();
            }else{
                throw new RealmException("AddressProvider.deleteAddressAsync couldn't delete any element with addressId " + addressId );
            }
        }, successHandler, errorHandler  );
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
                errors.add( new SubmitError(AddressFields.ADDRESSID, resourcesProvider.getString(R.string.address_gone)));
                return errors;
            }
        }

        //lets do a check for name
        if( SubmitError.emptyOrNull(address.getName()) ){
            errors.add( new SubmitError(AddressFields.NAME, resourcesProvider.getString(R.string.required_field)));
        }

        //if lat lon are provided we skip checking address and zipcode
        if( !SubmitError.initialized( address.getLat() ) && !SubmitError.initialized( address.getLon() ) ){

            if( SubmitError.emptyOrNull(address.getAddress()) ){
                errors.add( new SubmitError(AddressFields.ADDRESS, resourcesProvider.getString(R.string.required_field)));
            }

            if( SubmitError.emptyOrNull(address.getCity()) ){
                errors.add( new SubmitError(AddressFields.CITY, resourcesProvider.getString(R.string.required_field)));
            }
        }
        return errors;
    }
}