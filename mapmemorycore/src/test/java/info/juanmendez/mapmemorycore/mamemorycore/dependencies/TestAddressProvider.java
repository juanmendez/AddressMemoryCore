package info.juanmendez.mapmemorycore.mamemorycore.dependencies;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.mapmemorycore.dependencies.Response;
import info.juanmendez.mapmemorycore.dependencies.db.AddressProvider;
import info.juanmendez.mapmemorycore.models.MapAddress;
import info.juanmendez.mapmemorycore.models.MapMemoryException;
import info.juanmendez.mapmemorycore.models.SubmitError;
import rx.Observable;


/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestAddressProvider implements AddressProvider {

    List<MapAddress> addresses = new ArrayList<>();
    MapAddress selectedAddress;

    int totalAdded=0;

    @Override
    public MapAddress getSelectedAddress() {
        return selectedAddress;
    }

    @Override
    public void selectAddress(MapAddress selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    @Override
    public List<MapAddress> getAddresses() {
        return addresses;
    }

    @Override
    public Observable<List<MapAddress>> getAddressesAsync() {
        return null;
    }

    @Override
    public MapAddress getAddress(long addressId) {

        for(MapAddress address: addresses ){

            if( addressId == address.getAddressId() )
                return address;
        }

        return null;
    }

    @Override
    public Observable<MapAddress> getAddressAsync(long addressId) {
        return null;
    }

    @Override
    public MapAddress updateAddress(MapAddress updated) {

        //here we update
        for(MapAddress address: addresses ){

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
    public void updateAddressAsync(MapAddress address, Response<MapAddress> response) {
        MapAddress updated = updateAddress( address );

        if( updated != null ){
            response.onResult(updated);
        }else{
            response.onError( new MapMemoryException("couldn't update address asynchronously"));
        }
    }

    @Override
    public void deleteAddressAsync(long addressId, Response<MapAddress> response) {
        MapAddress address = getAddress( addressId);

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
    public List<SubmitError> validate(MapAddress address) {
        return null;
    }
}
