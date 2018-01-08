package info.juanmendez.mapmemorycore.addressmemorycore.dependencies;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.Response;
import info.juanmendez.addressmemorycore.models.AddressFields;
import info.juanmendez.addressmemorycore.models.AddressException;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.models.SubmitError;
import info.juanmendez.addressmemorycore.utils.AddressUtils;
import io.reactivex.Flowable;

/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestAddressProvider implements AddressProvider {

    private List<ShortAddress> mAddresses = new ArrayList<>();
    private ShortAddress mSelectedAddress = new ShortAddress();
    private long mLastId;

    int totalAdded=0;

    @Override
    public boolean connect() {
        return true;
    }

    @Override
    public boolean disconnect() {
        return true;
    }

    @Override
    public ShortAddress getSelectedAddress() {
        return mSelectedAddress;
    }

    @Override
    public void selectAddress(ShortAddress selectedAddress) {
        mSelectedAddress = AddressUtils.cloneAddress(selectedAddress);
    }

    @Override
    public List<ShortAddress> getAddresses() {
        return mAddresses;
    }

    @Override
    public Flowable<List<ShortAddress>> getAddressesAsync() {
        return Flowable.just(mAddresses);
    }

    @Override
    public ShortAddress getAddress(long addressId) {

        for(ShortAddress address: mAddresses){

            if( addressId == address.getAddressId() )
                return address;
        }

        return null;
    }

    @Override
    public Flowable<ShortAddress> getAddressAsync(long addressId) {
        return Flowable.just(getAddress(addressId));
    }

    @Override
    public List<ShortAddress> getClonedAddresses() {
        return mAddresses;
    }

    @Override
    public ShortAddress updateAddress(ShortAddress updated) {

        long currentId;

        //here we update
        for(ShortAddress address: mAddresses){
            currentId = updated.getAddressId();

            if( currentId != 0 && currentId == address.getAddressId() ){
                AddressUtils.copyAddress( updated, address );
                return address;
            }
        }

        //here we add
        updated.setAddressId(mLastId +=1);
        mAddresses.add( updated );
        totalAdded++;
        return updated;
    }

    @Override
    public void updateAddressAsync(ShortAddress address, Response<ShortAddress> response) {
        ShortAddress updated = updateAddress( address );

        if( updated != null ){
            response.onResult(updated);
        }else{
            response.onError( new AddressException("couldn't update address asynchronously"));
        }
    }

    @Override
    public void deleteAddress(long addressId) {
        ShortAddress address = getAddress( addressId);

        if( address != null ){
            mAddresses.remove(address);
        }
    }

    @Override
    public void deleteAddressAsync(long addressId, Response<Boolean> response) {
        ShortAddress address = getAddress( addressId);

        if( address != null ){
            mAddresses.remove(address);

            if( address == mSelectedAddress){
                mSelectedAddress = new ShortAddress();
            }

            response.onResult(true);
        }else{
            response.onError( new AddressException("couldn't deletePhoto address asynchronously"));
        }
    }

    @Override
    public long getNextPrimaryKey() {
        return totalAdded+1;
    }

    @Override
    public long countAddresses() {
        return mAddresses.size();
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
