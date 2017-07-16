package info.juanmendez.mapmemorycore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.mapmemorycore.dependencies.Navigation;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AddressResponse;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AddressService;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AddressesResponse;
import info.juanmendez.mapmemorycore.dependencies.network.NetworkResponse;
import info.juanmendez.mapmemorycore.dependencies.network.NetworkService;
import info.juanmendez.mapmemorycore.mamemorycore.TestApp;
import info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddress.TestAddressFragment;
import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.vp.vpAddress.AddressPresenter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.spy;


/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class TestingAddressServices {

    @Before
    public void before() throws Exception {
        MapCoreModule.setApp( new TestApp() );
    }

    /**
     allow user to enter a new address
        let user to fill in address by current geolocation
        let user enter address manually if offline
        let user pick one address from matching ones based on what she has entered so far
     allow user to edit a previous address
     user can edit address just as it is done above
     enable user to pick a photo or take one for her address
     */
    @Test
    public void textNetwork(){

        TestAddressFragment fragmentSpied = spy(new TestAddressFragment());
        AddressPresenter presenter = fragmentSpied.getPresenter();
        Whitebox.setInternalState(presenter, "view", fragmentSpied );

        //we are going to see if user is on edit mode!
        Navigation navigation = (Navigation) Whitebox.getInternalState(presenter, "navigation");
        NetworkService networkService = (NetworkService)   Whitebox.getInternalState(presenter, "networkService");

        AddressService addressServiceMocked = (AddressService) Whitebox.getInternalState(presenter, "addressService");

        doAnswer(invocation -> {
            NetworkResponse response = invocation.getArgumentAt(1, NetworkResponse.class);
            response.onStatusChanged(true);
            return null;
        }).when( networkService ).connect(Mockito.anyObject(), any(NetworkResponse.class));

        fragmentSpied.setActive(true, null);
        verify( fragmentSpied ).onAddressResult(any(), anyBoolean());


        List<Address> addresses = getAddresses();

        doAnswer(invocation -> {
            AddressesResponse response = invocation.getArgumentAt(1, AddressesResponse.class );
            response.onAddressResults( addresses );
            return null;
        }).when(addressServiceMocked).suggestAddress(anyString(), any(AddressesResponse.class));


        //view requests suggested addresses
        presenter.requestAddressSuggestions("0 N. State");
        verify( fragmentSpied ).onAddressesSuggested(anyList());


        doAnswer(invocation -> {
            AddressesResponse response = invocation.getArgumentAt(1, AddressesResponse.class );
            response.onAddressError( new Error("You could be offline!"));
            return null;
        }).when(addressServiceMocked).suggestAddress(anyString(), any(AddressesResponse.class));


        //view requests suggested addresses
        presenter.requestAddressSuggestions("0 N. State");
        verify( fragmentSpied ).onAddressError( any(Error.class));
    }


    @Test
    public void testGeolocation(){

        TestAddressFragment fragmentSpied = spy(new TestAddressFragment());
        AddressPresenter presenter = fragmentSpied.getPresenter();
        Whitebox.setInternalState(presenter, "view", fragmentSpied );

        //we are going to see if user is on edit mode!
        Navigation navigation = (Navigation) Whitebox.getInternalState(presenter, "navigation");
        NetworkService networkService = (NetworkService)   Whitebox.getInternalState(presenter, "networkService");

        AddressService addressServiceMocked = (AddressService) Whitebox.getInternalState(presenter, "addressService");

        doAnswer(invocation -> {
            AddressResponse response = invocation.getArgumentAt(0, AddressResponse.class );
            response.onAddressResult( getAddresses().get(0) );
            return null;
        }).when(addressServiceMocked).geolocateAddress(any(AddressResponse.class));

        //view requests addresses by geolocation
        presenter.requestAddressByGeolocation();
        verify( fragmentSpied ).onAddressResult( any(Address.class), anyBoolean());

        doAnswer(invocation -> {
            AddressResponse response = invocation.getArgumentAt(0, AddressResponse.class );
            response.onAddressError(new Error("oops"));
            return null;
        }).when(addressServiceMocked).geolocateAddress(any(AddressResponse.class));

        //view requests addresses by geolocation
        presenter.requestAddressByGeolocation();

        verify( fragmentSpied  ).onAddressError( any(Error.class) );
    }


    List<Address> getAddresses(){

        List<Address> addresses = new ArrayList<>();

        Address address;
        //lets add an address, and see if addressesView has updated its addresses
        address = new Address();
        address.setName( "1");
        address.setAddress1("0 N. State");
        address.setAddress2( "Chicago, 60641" );
        addresses.add( address );

        address = new Address();
        address.setName( "2");
        address.setAddress1("1 N. State");
        address.setAddress2( "Chicago, 60641" );
        addresses.add( address );

        address = new Address();
        address.setName( "3");
        address.setAddress1("2 N. State");
        address.setAddress2( "Chicago, 60641" );
        addresses.add( address );

        address = new Address();
        address.setName( "4");
        address.setAddress1("3 N. State");
        address.setAddress2( "Chicago, 60641" );
        addresses.add( address );

        return addresses;
    }
}
