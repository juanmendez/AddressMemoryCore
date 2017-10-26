package info.juanmendez.addressmemorycore.dependencies;

import java.util.List;

import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.models.SubmitError;
import rx.Observable;

/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AddressProvider {

    //getter-setter selectedAddress
    ShortAddress getSelectedAddress();
    void selectAddress(ShortAddress selectedAddress);

    //get addresses
    List<ShortAddress> getAddresses();
    Observable<List<ShortAddress>> getAddressesAsync();

    //get address
    ShortAddress getAddress(long addressId );
    Observable<ShortAddress> getAddressAsync(long addressId);
    List<ShortAddress> getClonedAddresses();

    //update address
    ShortAddress updateAddress(ShortAddress address);
    void updateAddressAsync(ShortAddress address, Response<ShortAddress> response );
    void deleteAddressAsync(long addressId, Response<Boolean> response );

    //utils
    long getNextPrimaryKey();
    long countAddresses();
    List<SubmitError> validate(ShortAddress address );
}
