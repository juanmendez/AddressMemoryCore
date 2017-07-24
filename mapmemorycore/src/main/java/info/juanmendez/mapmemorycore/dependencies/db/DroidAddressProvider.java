package info.juanmendez.mapmemorycore.dependencies.db;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import info.juanmendez.mapmemorycore.R;
import info.juanmendez.mapmemorycore.models.MapAddress;
import info.juanmendez.mapmemorycore.models.MapMemoryException;
import info.juanmendez.mapmemorycore.dependencies.Response;
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
    MapAddress selectedAddress;

    public DroidAddressProvider(Application application, Realm realm) {
        this.application = application;
        this.realm = realm;
    }

    public MapAddress getSelectedAddress() {
        return selectedAddress;
    }

    public void selectAddress(MapAddress selectedAddress) {
        this.selectedAddress = selectedAddress;
    }


    public List<MapAddress> getAddresses(){
        RealmResults<MapAddress> addresses;

        realm.beginTransaction();
            addresses = realm.where( MapAddress.class ).findAll();
        realm.commitTransaction();

        return addresses;
    }

    public Observable<List<MapAddress>> getAddressesAsync() {

        RealmResults<MapAddress> addresses;

        realm.beginTransaction();
        addresses = realm.where( MapAddress.class ).findAllAsync();
        realm.commitTransaction();

        return addresses.asObservable().map(results -> (List<MapAddress>)results);
    }

    public Observable<MapAddress> getAddressAsync(long addressId){

        return realm.where( MapAddress.class )
                .equalTo( "addressId", addressId )
                .findFirstAsync().asObservable();
    }

    public MapAddress getAddress(long addressId ){

        MapAddress address;

        realm.beginTransaction();
        address = realm.where( MapAddress.class ).equalTo( "addressId", addressId ).findFirst();
        realm.commitTransaction();

        return address;
    }


    public void updateAddressAsync(MapAddress address, Response<MapAddress> response ) {

        final MapAddress[] addressArray = new MapAddress[1];

        realm.executeTransactionAsync(thisRealm -> {
            addressArray[0] = thisRealm.copyToRealmOrUpdate(address);
        }, () -> {
            if (addressArray.length != 0) {
                response.onResult(addressArray[0]);
            }

        }, exception -> {
            response.onError(new MapMemoryException(exception.getMessage()));
        });
    }

    public MapAddress updateAddress(MapAddress address){

        MapAddress updatedAddress;

        realm.beginTransaction();
           updatedAddress = realm.copyToRealmOrUpdate( address );
        realm.commitTransaction();

        return updatedAddress;
    }

    public void deleteAddressAsync(long addressId, Response<MapAddress> response ){

        final MapAddress[] addressArray = new MapAddress[1];

        realm.executeTransactionAsync(thisRealm -> {
            addressArray[0] = thisRealm.where( MapAddress.class ).equalTo( "addressId", addressId ).findFirst();

            if( addressArray.length != 0  ){
                addressArray[0].deleteFromRealm();
            }else{
                throw new RealmException("Couldn't find an element to delete");
            }
        }, () -> {
            if( addressArray.length != 0 ){
                response.onResult(addressArray[0]);
            }

        }, exception -> { response.onError(new MapMemoryException(exception.getMessage()));}  );
    }

    public long getNextPrimaryKey(){
        AtomicLong primaryKeyValue;

        try {
            primaryKeyValue = new AtomicLong(realm.where(MapAddress.class).max("addressId").longValue());
        } catch (Exception e) {
            return 1;
        }

        return primaryKeyValue.incrementAndGet();
    }

    public long countAddresses(){
        return realm.where(MapAddress.class).count();
    }

    public List<SubmitError> validate(MapAddress address ){
        List<SubmitError> errors = new ArrayList<>();


        if( SubmitError.initialized( address.getAddressId() ) ){

            MapAddress savedAddress =  getAddress( address.getAddressId() );

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