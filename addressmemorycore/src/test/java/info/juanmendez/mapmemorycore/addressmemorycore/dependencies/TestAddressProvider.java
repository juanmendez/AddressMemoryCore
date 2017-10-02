package info.juanmendez.mapmemorycore.addressmemorycore.dependencies;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.addressmemorycore.dependencies.Response;
import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.models.AddressFields;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.models.MapMemoryException;
import info.juanmendez.addressmemorycore.models.SubmitError;
import rx.Observable;


/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestAddressProvider implements AddressProvider {

    List<ShortAddress> addresses = new ArrayList<>();
    ShortAddress selectedAddress = new ShortAddress();

    int totalAdded=0;

    @Override
    public ShortAddress getSelectedAddress() {
        return selectedAddress;
    }

    @Override
    public void selectAddress(ShortAddress selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    @Override
    public List<ShortAddress> getAddresses() {
        return addresses;
    }

    @Override
    public Observable<List<ShortAddress>> getAddressesAsync() {
        return null;
    }

    @Override
    public ShortAddress getAddress(long addressId) {

        for(ShortAddress address: addresses ){

            if( addressId == address.getAddressId() )
                return address;
        }

        return null;
    }

    @Override
    public Observable<ShortAddress> getAddressAsync(long addressId) {
        return null;
    }

    @Override
    public ShortAddress updateAddress(ShortAddress updated) {

        //here we update
        for(ShortAddress address: addresses ){

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
    public void updateAddressAsync(ShortAddress address, Response<ShortAddress> response) {
        ShortAddress updated = updateAddress( address );

        if( updated != null ){
            response.onResult(updated);
        }else{
            response.onError( new MapMemoryException("couldn't update address asynchronously"));
        }
    }

    @Override
    public void deleteAddressAsync(long addressId, Response<Boolean> response) {
        ShortAddress address = getAddress( addressId);

        if( address != null ){
            addresses.remove(address);
            response.onResult(true);
        }else{
            response.onError( new MapMemoryException("couldn't delete address asynchronously"));
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
    public List<SubmitError> validate(ShortAddress address) {
        List<SubmitError> errors = new ArrayList<>();

        if( SubmitError.emptyOrNull(address.getName()))
            errors.add( new SubmitError(AddressFields.NAME, "Name is missing"));

        if( SubmitError.emptyOrNull(address.getAddress1()))
            errors.add( new SubmitError(AddressFields.ADDRESS1, "Address is missing"));

        return errors;
    }
}