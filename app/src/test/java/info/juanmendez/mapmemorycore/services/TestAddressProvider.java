package info.juanmendez.mapmemorycore.services;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.mapmemorycore.models.Address;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestAddressProvider implements AddressProvider {

    private ArrayList<Address> addresses;

    public TestAddressProvider() {
        addresses = new ArrayList<>();
        addresses.add( new Address(1) );
        addresses.add( new Address(2) );
        addresses.add( new Address(3) );
        addresses.add( new Address(4) );
        addresses.add( new Address(5) );
    }

    @Override
    public void getAddresses(DisposableSubscriber<List<Address>> subscriber) {

        subscriber.onNext( addresses );
    }

    @Override
    public void getAddress(int addressId, DisposableSubscriber<Address> subscriber) {

        for( Address address: addresses ){
            if( address.getAddressId() == addressId ){
                subscriber.onNext( address );
                return;
            }
        }

        subscriber.onError( new Exception("address not found") );
    }

    @Override
    public void updateAddress(Address address, DisposableSubscriber<Address> subscriber) {

        if( addresses.contains( address )){
            subscriber.onNext( address );
            return;
        }

        subscriber.onError( new Exception("address not found") );
    }

    @Override
    public void deleteAddress(int addressId, DisposableSubscriber<Address> subscriber) {
        Address deletedAddress = null;


        for( Address address: addresses ){
            if( address.getAddressId() == addressId ){
                deletedAddress = address;
                break;
            }
        }

        if( deletedAddress != null ){
            addresses.remove( deletedAddress );
            subscriber.onNext( deletedAddress );
        }
        else{
            subscriber.onError( new Exception("address not found") );
        }

    }
}
