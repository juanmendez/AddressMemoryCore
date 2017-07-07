package info.juanmendez.mapmemorycore.mamemorycore.dependencies;

import java.util.List;

import info.juanmendez.mapmemorycore.dependencies.db.AddressProvider;
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

public class TestAddressProvider implements AddressProvider {
    @Override
    public Address getSelectedAddress() {
        return null;
    }

    @Override
    public void selectAddress(Address selectedAddress) {

    }

    @Override
    public RealmResults<Address> getAddresses() {
        return null;
    }

    @Override
    public Observable<List<Address>> getAddressesAsync() {
        return null;
    }

    @Override
    public Address getAddress(long addressId) {
        return null;
    }

    @Override
    public Observable<Address> getAddressAsync(long addressId) {
        return null;
    }

    @Override
    public Address updateAddress(Address address) {
        return null;
    }

    @Override
    public void updateAddressAsync(Address address, Realm.Transaction.OnSuccess successHandler, Realm.Transaction.OnError errorHandler) {

    }

    @Override
    public void deleteAddressAsync(long addressId, Realm.Transaction.OnSuccess successHandler, Realm.Transaction.OnError errorHandler) {

    }

    @Override
    public long getNextPrimaryKey() {
        return 0;
    }

    @Override
    public long countAddresses() {
        return 0;
    }

    @Override
    public List<SubmitError> validate(Address address) {
        return null;
    }
}
