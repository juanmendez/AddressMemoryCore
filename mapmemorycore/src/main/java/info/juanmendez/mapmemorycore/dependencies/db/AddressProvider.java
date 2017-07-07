package info.juanmendez.mapmemorycore.dependencies.db;

import java.util.List;

import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.models.SubmitError;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;

/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AddressProvider {

    //getter-setter selectedAddress
    Address getSelectedAddress();
    void selectAddress(Address selectedAddress);

    //get addresses
    RealmResults<Address> getAddresses();
    Observable<RealmResults<Address>> getAddressesAsync();

    //get address
    Address getAddress( long addressId );
    Observable<Address> getAddressAsync(long addressId);

    //update address
    Address updateAddress(Address address);
    void updateAddressAsync(Address address, Realm.Transaction.OnSuccess successHandler, Realm.Transaction.OnError errorHandler );

    void deleteAddressAsync(long addressId, Realm.Transaction.OnSuccess successHandler, Realm.Transaction.OnError errorHandler );

    //utils
    long getNextPrimaryKey();
    long countAddresses();
    List<SubmitError> validate(Address address );
}
