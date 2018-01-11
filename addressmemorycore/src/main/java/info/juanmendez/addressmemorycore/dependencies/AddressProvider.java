package info.juanmendez.addressmemorycore.dependencies;

import java.util.List;

import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.models.SubmitError;
import io.reactivex.Flowable;

/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AddressProvider {

    //these methods are needed for component lifecycle
    boolean connect();
    boolean disconnect();

    //getter-setter selectedAddress
    ShortAddress getSelectedAddress();
    void selectAddress(ShortAddress selectedAddress);

    //get addresses
    List<ShortAddress> getAddresses();
    Flowable<List<ShortAddress>> getAddressesAsync();

    //get address
    ShortAddress getAddress(long addressId );
    Flowable<ShortAddress> getAddressAsync(long addressId);
    List<ShortAddress> getClonedAddresses();

    //update address
    ShortAddress updateAddress(ShortAddress address);
    void updateAddressAsync(ShortAddress address, Response<ShortAddress> response );

    void deleteAddress( long addressId );
    void deleteAddresses();
    void deleteAddressAsync(long addressId, Response<Boolean> response );

    //utils
    long getNextPrimaryKey();
    long countAddresses();
    List<SubmitError> validate(ShortAddress address );
}
