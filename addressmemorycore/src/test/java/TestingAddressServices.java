import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.AddressService;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.QuickResponse;
import info.juanmendez.addressmemorycore.dependencies.Response;
import info.juanmendez.addressmemorycore.models.AddressViewModel;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.models.SuggestAddressViewModel;
import info.juanmendez.addressmemorycore.modules.MapModuleBase;
import info.juanmendez.addressmemorycore.vp.FragmentNav;
import info.juanmendez.addressmemorycore.vp.vpAddress.AddressPresenter;
import info.juanmendez.addressmemorycore.vp.vpAddress.AddressView;
import info.juanmendez.addressmemorycore.vp.vpSuggest.SuggestPresenter;
import info.juanmendez.addressmemorycore.vp.vpSuggest.SuggestView;
import info.juanmendez.mapmemorycore.addressmemorycore.TestApp;
import info.juanmendez.mapmemorycore.addressmemorycore.module.DaggerMapCoreComponent;
import info.juanmendez.mapmemorycore.addressmemorycore.module.MapCoreModule;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;


/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class TestingAddressServices {

    List<ShortAddress> addresses = new ArrayList<>();

    AddressView addressView;
    AddressPresenter addressPresenter;
    AddressViewModel addressViewModel;

    SuggestView suggestView;
    SuggestPresenter suggestPresenter;
    SuggestAddressViewModel suggestViewModel;

    ShortAddress selectedAddress;


    NetworkService networkServiceMocked;
    AddressService addressServiceMocked;
    AddressProvider addressProvider;
    NavigationService navigationService;
    String navigationTag = "helloNavigation";


    @Before
    public void before() throws Exception {

        MapModuleBase.setInjector( DaggerMapCoreComponent.builder().mapCoreModule(new MapCoreModule(new TestApp())).build() );

        addressView = mock( AddressView.class );
        addressPresenter = new AddressPresenter();
        addressViewModel = addressPresenter.getViewModel(addressView);

        suggestView = mock( SuggestView.class );
        suggestPresenter = new SuggestPresenter();
        suggestViewModel = suggestPresenter.getViewModel( suggestView );

        networkServiceMocked = Whitebox.getInternalState(addressPresenter, "networkService");
        addressServiceMocked = Whitebox.getInternalState(addressPresenter, "addressService");
        addressProvider = Whitebox.getInternalState(suggestPresenter, "addressProvider");
        navigationService = Whitebox.getInternalState(addressPresenter, "navigationService");

        //addressService and networkService are not singletons, so we want to save ourselves doing extra work
        //by using the same mocked objects from presenter
        Whitebox.setInternalState( suggestPresenter, "addressService",addressServiceMocked );
        Whitebox.setInternalState( suggestPresenter, "networkService",networkServiceMocked );

        //make each mocked object answer with positive results such as networkService.isConnected() returning true.
        applySuccessfulResults();
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
    public void testInitialPhase(){

        addressPresenter.active("");

        addressViewModel.setName("First address");
        addressPresenter.requestAddressByGeolocation();
        verify(addressServiceMocked).geolocateAddress(any(Response.class));

        selectedAddress = addressProvider.getSelectedAddress();

        assertFalse(selectedAddress.getName().isEmpty());
        assertFalse(selectedAddress.getAddress1().isEmpty());
        assertFalse(selectedAddress.getAddress2().isEmpty());

        addressPresenter.inactive(false);

        //we switch now to suggestPresenter, we want to ensure
        //when landing there are already matching addresses!
        suggestPresenter.active("");
        assertFalse( suggestViewModel.getMatchingAddresses().isEmpty() );

        //how about clearing the address edited. There should be no matching addresses
        suggestViewModel.setAddressEdited("");
        assertTrue( suggestViewModel.getMatchingAddresses().isEmpty() );

        //how about spaces?
        suggestViewModel.setAddressEdited("  ");
        assertTrue( suggestViewModel.getMatchingAddresses().isEmpty() );

        //if clearing the address edited, then there shouldn't be any matching addressses!
        suggestViewModel.setAddressEdited("100 N. LaSalle");
        assertFalse( suggestViewModel.getMatchingAddresses().isEmpty() );

        //we are going to have the addressService as disconnected.
        doReturn(false).when( addressServiceMocked ).isConnected();

        //now there is no service, there should be an exception found in the viewModel!
        suggestViewModel.setAddressEdited("200 N. LaSalle");
        assertFalse(suggestViewModel.getAddressException().getMessage().isEmpty());

        //revert
        doReturn(true).when( addressServiceMocked ).isConnected();

        suggestViewModel.setAddressEdited("100 N. LaSalle");
        assertFalse( suggestViewModel.getMatchingAddresses().isEmpty() );

        //we have matching addresses, and we are going to pick on the second one as our selected address!
        ShortAddress pickedAddress = suggestViewModel.getMatchingAddresses().get(1);
        suggestViewModel.setPickedMatchingAddress( pickedAddress );

        //BINGO!!
        assertEquals( selectedAddress.getAddress1(), pickedAddress.getAddress1() );
        assertEquals( selectedAddress.getAddress2(), pickedAddress.getAddress2() );
    }

    @Test
    public void testNavigationTag(){
        assertEquals( navigationService.getNavigationTag(addressView), navigationTag );

        doReturn( null ).when( navigationService ).getNavigationTag(eq(addressView));
        assertNull( navigationService.getNavigationTag(addressView));
    }

    /**
     * test geolocation with successful and error response
     */
    @Test
    public void testGeolocation(){

      /*  //view suggested address by geolocation
        presenter.requestAddressByGeolocation();
        verify(addressView).onAddressResult( any(ShortAddress.class));

        reset(addressView);

        //make it response with an error
        doAnswer(invocation -> {
            Response<ShortAddress> response = invocation.getArgumentAt(0, Response.class );
            response.onError(new MapMemoryException("oops"));
            return null;
        }).when(addressServiceMocked).geolocateAddress(any(Response.class));

        //view requests addresses by geolocation
        presenter.requestAddressByGeolocation();
        verify(addressView).onAddressError( any(Exception.class) );*/
    }

    Matcher<File> fileMatcher(final String location) {
        return new TypeSafeMatcher<File>() {
            public boolean matchesSafely(File item) {
                return location.equals(item.getAbsolutePath());
            }
            public void describeTo(Description description) {
                description.appendText("checks if file's path is " + location);
            }
        };
    }

    void applySuccessfulResults(){
        setAddresses();

        doReturn(true).when(networkServiceMocked).isConnected();

        doAnswer(invocation -> {
            QuickResponse<Boolean> response = invocation.getArgumentAt(0, Response.class);
            response.onResult(true);
            return null;
        }).when( networkServiceMocked ).connect(any(QuickResponse.class));

        doReturn(true).when( addressServiceMocked ).isConnected();

        doAnswer( invocation -> {
            Response<List<ShortAddress>> response = invocation.getArgumentAt(1, Response.class );
            response.onResult(addresses);

            return null;
        }).when( addressServiceMocked ).suggestAddress( anyString(), any(Response.class) );

        doAnswer( invocation -> {
            Response<ShortAddress> response = invocation.getArgumentAt(0, Response.class );
            response.onResult(addresses.get(0));

            return null;
        }).when( addressServiceMocked ).geolocateAddress( any(Response.class) );




        doReturn( navigationTag ).when( navigationService ).getNavigationTag(any(FragmentNav.class));

        for(ShortAddress address: addresses ){
            addressProvider.updateAddress( address );
        }
    }

    private List<ShortAddress> setAddresses(){

        ShortAddress address;
        //lets add an address, and see if addressesView has updated its addresses
        address = new ShortAddress();
        address.setName( "1");
        address.setAddress1("0 N. State");
        address.setAddress2( "Chicago, 60641" );
        addresses.add( address );

        address = new ShortAddress();
        address.setName( "2");
        address.setAddress1("1 N. State");
        address.setAddress2( "Chicago, 60641" );
        addresses.add( address );

        address = new ShortAddress();
        address.setName( "3");
        address.setAddress1("2 N. State");
        address.setAddress2( "Chicago, 60641" );
        addresses.add( address );

        address = new ShortAddress();
        address.setName( "4");
        address.setAddress1("3 N. State");
        address.setAddress2( "Chicago, 60641" );
        addresses.add( address );

        return addresses;
    }
}