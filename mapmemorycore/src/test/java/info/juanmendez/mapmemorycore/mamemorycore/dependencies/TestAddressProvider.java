package info.juanmendez.mapmemorycore.mamemorycore.dependencies;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.mapmemorycore.dependencies.Response;
import info.juanmendez.mapmemorycore.dependencies.db.AddressProvider;
import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.models.MapMemoryException;
import info.juanmendez.mapmemorycore.models.SubmitError;
import rx.Observable;


/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestAddressProvider implements AddressProvider {

    List<ShortAddress> addresses = new ArrayList<>();
    ShortAddress selectedAddress;

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
    public void deleteAddressAsync(long addressId, Response<ShortAddress> response) {
        ShortAddress address = getAddress( addressId);

        if( address != null ){
            addresses.remove(address);
            response.onResult(address);
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
        return new ArrayList<SubmitError>();
    }
}
