package info.juanmendez.mapmemorycore.mamemorycore.dependencies;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.mapmemorycore.dependencies.db.AddressProvider;
import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.models.SubmitError;
import io.realm.Realm;
import rx.Observable;


/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestAddressProvider implements AddressProvider {

    List<Address> addresses = new ArrayList<>();
    Address selectedAddress;

    int totalAdded=0;

    @Override
    public Address getSelectedAddress() {
        return selectedAddress;
    }

    @Override
    public void selectAddress(Address selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    @Override
    public List<Address> getAddresses() {
        return addresses;
    }

    @Override
    public Observable<List<Address>> getAddressesAsync() {
        return null;
    }

    @Override
    public Address getAddress(long addressId) {

        for(Address address: addresses ){

            if( addressId == address.getAddressId() )
                return address;
        }

        return null;
    }

    @Override
    public Observable<Address> getAddressAsync(long addressId) {
        return null;
    }

    @Override
    public Address updateAddress(Address updated) {

        //here we update
        for(Address address: addresses ){

            if( updated.getAddressId() == address.getAddressId() ){
                int location = addresses.indexOf(address);

                return addresses.set( location, updated );
            }

        }

        //here we add
        addresses.add( updated );
        totalAdded++;
        return updated;
    }

    @Override
    public void updateAddressAsync(Address address, Realm.Transaction.OnSuccess successHandler, Realm.Transaction.OnError errorHandler) {
        Address updated = updateAddress( address );

        if( updated != null ){
            successHandler.onSuccess();
        }else{
            errorHandler.onError( new Exception("couldn't update address asynchronously"));
        }
    }

    @Override
    public void deleteAddressAsync(long addressId, Realm.Transaction.OnSuccess successHandler, Realm.Transaction.OnError errorHandler) {
        Address address = getAddress( addressId);

        if( address != null ){
            addresses.remove(address);
            successHandler.onSuccess();
        }else{
            errorHandler.onError( new Exception("couldn't delete address asynchronously"));
        }
    }

    @Override
    public long getNextPrimaryKey() {
        return totalAdded+1;
    }

    @Override
    public long countAddresses() {
        return addresses.size();
    }

    @Override
    public List<SubmitError> validate(Address address) {
        return null;
    }
}
