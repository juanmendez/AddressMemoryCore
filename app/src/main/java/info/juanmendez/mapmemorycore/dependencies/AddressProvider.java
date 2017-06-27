package info.juanmendez.mapmemorycore.dependencies;

import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import info.juanmendez.mapmemorycore.models.Address;
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

    @Inject
    public AddressProvider(RealmProvider realmProvider) {
        this.realm = realmProvider.getRealm();
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

    public Observable<Address> getAddressAsync(int addressId){

        return realm.where( Address.class )
                .equalTo( "addressId", addressId )
                .findFirstAsync().asObservable();
    }

    public Address getAddress( int addressId ){

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
           updatedAddress = realm.copyToRealm( address );
        realm.commitTransaction();

        return updatedAddress;
    }

    public void deleteAddressAsync(int addressId, Realm.Transaction.OnSuccess successHandler, Realm.Transaction.OnError errorHandler ){
        realm.executeTransactionAsync(thisRealm -> {
            RealmResults<Address> addresses = thisRealm.where( Address.class ).equalTo( "addressId", addressId ).findAll();

            if( !addresses.isEmpty() ){
                addresses.deleteAllFromRealm();
            }else{
                throw new RealmException("AddressProvider.deleteAddressAsync couldn't delete any element with addressId " + addressId );
            }
        }, successHandler, errorHandler  );
    }

    public int getNextPrimaryKey(){
        AtomicInteger primaryKeyValue;

        try {
            primaryKeyValue = new AtomicInteger(realm.where(Address.class).max("addressId").intValue());
        } catch (Exception e) {
            return 1;
        }

        return primaryKeyValue.incrementAndGet();
    }

}