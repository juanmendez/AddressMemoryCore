package info.juanmendez.mapmemorycore.services;

import java.util.List;

import info.juanmendez.mapmemorycore.models.Address;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AddressProvider {
    void getAddresses(DisposableSubscriber<List<Address>> subscriber ) ;
    void getAddress( int addressId,  DisposableSubscriber<Address> subscriber );
    void updateAddress( Address address, DisposableSubscriber<Address> subscriber );
    void deleteAddress( int addressId, DisposableSubscriber<Address> subscriber );
}