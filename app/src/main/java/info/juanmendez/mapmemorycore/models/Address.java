package info.juanmendez.mapmemorycore.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */


public class Address extends RealmObject{

    @PrimaryKey
    private int addressId;

    public Address() {
    }

    public Address(int addressId) {
        this.addressId = addressId;
    }

    public int getAddressId() {
        return addressId;
    }
}
