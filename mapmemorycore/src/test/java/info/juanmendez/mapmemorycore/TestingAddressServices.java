package info.juanmendez.mapmemorycore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.mapmemorycore.dependencies.Response;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AddressService;
import info.juanmendez.mapmemorycore.dependencies.network.NetworkService;
import info.juanmendez.mapmemorycore.mamemorycore.TestApp;
import info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddress.TestAddressFragment;
import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.models.MapMemoryException;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.vp.vpAddress.AddressFragment;
import info.juanmendez.mapmemorycore.vp.vpAddress.AddressPresenter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;
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

        //spying means using fragmentSpied reference in our presenter.
        AddressPresenter presenter = fragmentSpied.getPresenter();
        Whitebox.setInternalState(presenter, "view", fragmentSpied );

        NetworkService networkServiceMocked = (NetworkService)   Whitebox.getInternalState(presenter, "networkService");
        AddressService addressServiceMocked = (AddressService) Whitebox.getInternalState(presenter, "addressService");

        doReturn(true).when(networkServiceMocked).isConnected();

        doAnswer(invocation -> {
            Response<Boolean> response = invocation.getArgumentAt(0, Response.class);
            response.onResult(true);
            return null;
        }).when( networkServiceMocked ).connect(any(Response.class));

        fragmentSpied.setActive(true, null);
        verify( fragmentSpied ).onAddressResult(any(), anyBoolean());


        List<Address> addresses = getAddresses();

        doAnswer(invocation -> {
            Response<List<Address>> response = invocation.getArgumentAt(1, Response.class );
            response.onResult( addresses );
            return null;
        }).when(addressServiceMocked).suggestAddress(anyString(), any(Response.class));


        //view requests suggested addresses
        presenter.requestAddressSuggestions("0 N. State");
        verify( fragmentSpied ).onAddressesSuggested(anyList());


        doAnswer(invocation -> {
            Response<Address> response = invocation.getArgumentAt(1, Response.class );
            response.onError( new MapMemoryException("You could be offline!"));
            return null;
        }).when(addressServiceMocked).suggestAddress(anyString(), any(Response.class));


        //view requests suggested addresses
        presenter.requestAddressSuggestions("0 N. State");
        verify( fragmentSpied ).onAddressError( any(Exception.class));
    }


    /**
     * test geolocation with successful and error response
     */
    @Test
    public void testGeolocation(){

        AddressFragment fragment = mock( AddressFragment.class );
        AddressPresenter presenter = new AddressPresenter();
        presenter.register( fragment );

        //Navigation navigation = (Navigation) Whitebox.getInternalState(presenter, "navigation");
        NetworkService networkServiceMocked = (NetworkService)   Whitebox.getInternalState(presenter, "networkService");
        AddressService addressServiceMocked = (AddressService) Whitebox.getInternalState(presenter, "addressService");

        doReturn(true).when(networkServiceMocked).isConnected();

        doAnswer(invocation -> {
            Response<Address> response = invocation.getArgumentAt(0, Response.class );
            response.onResult( getAddresses().get(0) );
            return null;
        }).when(addressServiceMocked).geolocateAddress(any(Response.class));

        //view suggested address by geolocation
        presenter.requestAddressByGeolocation();
        verify( fragment ).onAddressResult( any(Address.class), anyBoolean());

        doAnswer(invocation -> {
            Response<Address> response = invocation.getArgumentAt(0, Response.class );
            response.onError(new MapMemoryException("oops"));
            return null;
        }).when(addressServiceMocked).geolocateAddress(any(Response.class));

        //view requests addresses by geolocation
        presenter.requestAddressByGeolocation();
        verify( fragment  ).onAddressError( any(Exception.class) );
    }

    @Test
    public void testSuggestion(){

        AddressFragment fragment = mock( AddressFragment.class );
        AddressPresenter presenter = new AddressPresenter();
        presenter.register( fragment );

        NetworkService networkServiceMocked = (NetworkService)   Whitebox.getInternalState(presenter, "networkService");
        AddressService addressServiceMocked = (AddressService) Whitebox.getInternalState(presenter, "addressService");

        doReturn(true).when(networkServiceMocked).isConnected();

        doAnswer( invocation -> {
            Response<List<Address>> response = invocation.getArgumentAt(1, Response.class );
            response.onResult(new ArrayList<Address>());

            return null;
        }).when( addressServiceMocked ).suggestAddress( anyString(), any(Response.class) );

        presenter.requestAddressSuggestions( "3463 N. Natch" );
        verify( fragment ).onAddressesSuggested(anyList());


        //we want to make an exception happen during addressService.suggestAddress
        //by providing an empty query.
        presenter.requestAddressSuggestions( "" );
        verify( fragment ).onAddressError(any(Exception.class));

        reset(fragment);

        //ok we want to catch an error if there is no connection
        doReturn(false).when(networkServiceMocked).isConnected();
        presenter.requestAddressSuggestions( "3463 N. Natch" );
        verify( fragment ).onAddressError(any(Exception.class));
    }

    //util
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