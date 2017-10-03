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
import info.juanmendez.addressmemorycore.dependencies.Response;
import info.juanmendez.addressmemorycore.models.AddressViewModel;
import info.juanmendez.mapmemorycore.addressmemorycore.TestApp;
import info.juanmendez.mapmemorycore.addressmemorycore.module.DaggerMapCoreComponent;
import info.juanmendez.mapmemorycore.addressmemorycore.module.MapCoreModule;
import info.juanmendez.addressmemorycore.models.MapMemoryException;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.models.SubmitError;
import info.juanmendez.addressmemorycore.modules.MapModuleBase;
import info.juanmendez.addressmemorycore.vp.FragmentNav;
import info.juanmendez.addressmemorycore.vp.vpAddress.AddressPresenter;
import info.juanmendez.addressmemorycore.vp.vpAddress.AddressView;
import info.juanmendez.addressmemorycore.vp.vpSuggest.SuggestPresenter;
import info.juanmendez.addressmemorycore.vp.vpSuggest.SuggestView;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;


/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class TestingAddressServices {

    AddressView addressView;
    AddressPresenter addressPresenter;

    SuggestView suggestView;
    SuggestPresenter suggestPresenter;
    NetworkService networkServiceMocked;
    AddressService addressServiceMocked;
    AddressProvider addressProvider;
    NavigationService navigationService;
    String navigationTag = "helloNavigation";

    AddressViewModel viewModel;


    @Before
    public void before() throws Exception {
        MapModuleBase.setInjector( DaggerMapCoreComponent.builder().mapCoreModule(new MapCoreModule(new TestApp())).build() );

        addressView = mock( AddressView.class );
        addressPresenter = new AddressPresenter();
        viewModel = addressPresenter.getViewModel(addressView);


        suggestView = mock( SuggestView.class );
        suggestPresenter = new SuggestPresenter();
        suggestPresenter.getViewModel( suggestView );

        networkServiceMocked = (NetworkService)   Whitebox.getInternalState(addressPresenter, "networkService");
        addressServiceMocked = (AddressService) Whitebox.getInternalState(addressPresenter, "addressService");
        addressProvider = (AddressProvider) Whitebox.getInternalState(suggestPresenter, "addressProvider");
        navigationService = (NavigationService) Whitebox.getInternalState(addressPresenter, "navigationService");

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
    public void textNetwork(){

        addressPresenter.active("");
        assertTrue( viewModel.isOnline.get() );


        //view requests suggested addresses
        suggestPresenter.active("");
        suggestPresenter.requestAddressSuggestions("0 N. State");
        verify(suggestView).setSuggestedAddresses(anyList());


        //make it reply with an exception
        doAnswer(invocation -> {
            Response<ShortAddress> response = invocation.getArgumentAt(1, Response.class );
            response.onError( new MapMemoryException("You could be offline!"));
            return null;
        }).when(addressServiceMocked).suggestAddress(anyString(), any(Response.class));


        //view requests suggested addresses
        suggestPresenter.requestAddressSuggestions("0 N. State");
        verify(suggestView).onError( any(Exception.class));

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

    @Test
    public void testSuggestion(){

        /**
         * the user looks for suggested addresses, selects one
         * and it is verified if the presenter's editAddress is the first one from
         * getAddresses()
         */
        doAnswer(invocation -> {
            List<ShortAddress> addresses = invocation.getArgumentAt(0, List.class );

            //lets pick the first address..
            addressProvider.selectAddress( addresses.get(0));
            return null;
        }).when(suggestView).setSuggestedAddresses(anyList());

        suggestPresenter.active(null);
        suggestPresenter.requestAddressSuggestions( "3463 N. Natch" );
        verify(suggestView).setSuggestedAddresses(anyList());
        assertEquals( addressProvider.getSelectedAddress().getAddressId(), getAddresses().get(0).getAddressId() );

        //we want to make an exception happen during addressService.suggestAddress
        //by providing an empty query.
        suggestPresenter.requestAddressSuggestions( "" );
        verify(suggestView).onError(any(Exception.class));

        reset(addressView);

        //ok we want to catch an error if there is no connection
        doReturn(false).when(networkServiceMocked).isConnected();
        suggestPresenter.requestAddressSuggestions( "3463 N. Natch" );
        verify(suggestView, times(2)).onError(any(Exception.class));
    }

    @Test
    public void testCreatingAddress(){

        /*String fileLocation = "absolute_path";

        presenter.requestAddressByGeolocation();
        verify(addressView).onAddressResult( any(ShortAddress.class));

        AddressPresenter spiedPresenter = spy(presenter);
        doAnswer(invocation -> {
            Response<ShortAddress> response = invocation.getArgumentAt(0, Response.class );
            response.onResult(new ShortAddress());
            return null;
        }).when(spiedPresenter).saveAddress(any(Response.class));

        Response<ShortAddress> response = mock( Response.class );

        spiedPresenter.saveAddress(response);
        verify( response ).onResult(any(ShortAddress.class));*/
    }

    @Test
    public void testSubmittingAddressWithError(){

        String fileLocation = "absolute_path";
        String errorCode = "funkyError";

        AddressProvider spiedProvider = spy( addressProvider );
        Whitebox.setInternalState(addressPresenter, "addressProvider", spiedProvider );

        doAnswer( invocation -> {
            List<SubmitError> errors = new ArrayList<SubmitError>();
            errors.add( new SubmitError(errorCode, "lucky"));
            return errors;
        }).when( spiedProvider ).validate(any(ShortAddress.class));

        spiedProvider.selectAddress( new ShortAddress());

        Response<ShortAddress> response = mock( Response.class );

        addressPresenter.saveAddress( response );
        verify( response ).onError(any(MapMemoryException.class));

        response = mock( Response.class );

        when( spiedProvider.validate(any(ShortAddress.class)) ).thenReturn( new ArrayList<SubmitError>());

        doAnswer(invocation -> {
            ShortAddress address = invocation.getArgumentAt(0, ShortAddress.class );
            Response<ShortAddress>  thisResponse = invocation.getArgumentAt(1, Response.class );
            thisResponse.onResult( address );
            return null;
        }).when(spiedProvider).updateAddressAsync(any(ShortAddress.class), any(Response.class));

        addressPresenter.saveAddress( response );
        verify( response ).onResult(any(ShortAddress.class));
    }

    @Test
    public void testSubmittingAddress(){

        String fileLocation = "absolute_path";

        AddressProvider spiedProvider = spy( addressProvider );
        Whitebox.setInternalState(addressPresenter, "addressProvider", spiedProvider );

        spiedProvider.selectAddress( new ShortAddress());
        when( spiedProvider.validate(any(ShortAddress.class)) ).thenReturn( new ArrayList<SubmitError>());

        doAnswer(invocation -> {
            ShortAddress address = invocation.getArgumentAt(0, ShortAddress.class );
            Response<ShortAddress>  thisResponse = invocation.getArgumentAt(1, Response.class );
            thisResponse.onResult( address );
            return null;
        }).when(spiedProvider).updateAddressAsync(any(ShortAddress.class), any(Response.class));

        Whitebox.setInternalState(addressPresenter, "addressProvider", spiedProvider );

        Response<ShortAddress> response = mock( Response.class );
        addressPresenter.saveAddress( response );

        verify( response ).onResult(any(ShortAddress.class));
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

        String fileLocation = "absolute_path";

        doReturn(true).when(networkServiceMocked).isConnected();

        doAnswer(invocation -> {
            Response<Boolean> response = invocation.getArgumentAt(0, Response.class);
            response.onResult(true);
            return null;
        }).when( networkServiceMocked ).connect(any(Response.class));

        doAnswer( invocation -> {
            Response<List<ShortAddress>> response = invocation.getArgumentAt(1, Response.class );
            response.onResult(getAddresses());

            return null;
        }).when( addressServiceMocked ).suggestAddress( anyString(), any(Response.class) );

        doAnswer( invocation -> {
            Response<ShortAddress> response = invocation.getArgumentAt(0, Response.class );
            response.onResult(getAddresses().get(0));

            return null;
        }).when( addressServiceMocked ).geolocateAddress( any(Response.class) );


        doReturn( navigationTag ).when( navigationService ).getNavigationTag(any(FragmentNav.class));
    }

    List<ShortAddress> getAddresses(){

        List<ShortAddress> addresses = new ArrayList<>();

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