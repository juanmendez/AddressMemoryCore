package info.juanmendez.mapmemorycore.dependencies;

import javax.inject.Inject;
import javax.inject.Singleton;

import info.juanmendez.mapmemorycore.models.Address;
import io.reactivex.subscribers.DisposableSubscriber;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

@Singleton
public class AddressProvider {
    @Inject
    Realm realmProvider;

    void getAddresses(DisposableSubscriber<RealmResults<Address>> subscriber ){

    }

    void getAddress( int addressId,  DisposableSubscriber<Address> subscriber ){

    }

    void updateAddress( Address address, DisposableSubscriber<Address> subscriber ){

    }


    void deleteAddress( int addressId, DisposableSubscriber<Address> subscriber ){

    }
}