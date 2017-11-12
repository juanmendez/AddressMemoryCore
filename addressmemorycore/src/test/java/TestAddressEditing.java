import android.app.Activity;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.AddressService;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.QuickResponse;
import info.juanmendez.addressmemorycore.dependencies.Response;
import info.juanmendez.addressmemorycore.models.Commute;
import info.juanmendez.addressmemorycore.models.MapMemoryException;
import info.juanmendez.addressmemorycore.models.RouteMessage;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.models.SubmitError;
import info.juanmendez.addressmemorycore.vp.FragmentNav;
import info.juanmendez.addressmemorycore.vp.vpAddress.AddressPresenter;
import info.juanmendez.addressmemorycore.vp.vpAddress.AddressView;
import info.juanmendez.addressmemorycore.vp.vpAddress.AddressViewModel;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Juan Mendez on 9/27/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class TestAddressEditing extends  TestAddressMemoryCore {
    AddressView addressView;
    AddressPresenter presenter;

    NetworkService networkServiceMocked;
    AddressService addressServiceMocked;
    NavigationService navigationService;
    AddressProvider provider;

    String navigationTag = "helloNavigation";
    List<ShortAddress> addresses = new ArrayList<>();

    AddressViewModel viewModel;

    @Before
    public void before() throws Exception {

        addressView = mock( AddressView.class );
        presenter = spy(new AddressPresenter(m));
        viewModel = presenter.getViewModel(addressView);

        networkServiceMocked = m.getNetworkService();
        addressServiceMocked = m.getAddressService();
        navigationService = m.getNavigationService();
        provider = m.getAddressProvider();
        provider = spy(provider);
        Whitebox.setInternalState(presenter,"addressProvider", provider);

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

        presenter.active("");
        assertTrue( viewModel.isOnline.get() );
        presenter.inactive(false);
    }

    @Test
    public void testAddressValidation(){
        presenter.active("");
        assertFalse( viewModel.canSubmit.get() );

        ShortAddress address = viewModel.getAddress();
        viewModel.setAddress1("00 N. State");
        assertFalse( viewModel.canSubmit.get() );

        viewModel.setName("Home");
        assertTrue( viewModel.canSubmit.get() );

        viewModel.setName("");
        viewModel.notifyAddress();
        assertFalse( viewModel.canSubmit.get() );

        //address without id cannot be deleted!
        assertFalse( viewModel.canDelete.get() );

        //then add the id
        address.setAddressId(1);
        viewModel.notifyAddress();
        assertTrue( viewModel.canDelete.get() );

        presenter.inactive(false);
    }

    /**
     * Through the suggestViewModel we are able to determine
     * if user is editng address1, or address2.
     * If there is any geolocation update then we use those values
     * to figure out for editing from the presenter rather than the view.
     */
    @Test
    public void requestAddressSuggestion(){
        presenter.active("");

        //ok editing before geolocation update
        reset(presenter);
        viewModel.setAddress2(viewModel.getAddress2() + "...");
        verify(presenter).requestAddressSuggestion();


        //now we request an update for geolocation.
        presenter.requestAddressByGeolocation();

        ShortAddress geoResult = Whitebox.getInternalState(presenter, "mGeoResult");
        assertFalse( geoResult.getAddress1().isEmpty() );
        assertFalse( geoResult.getAddress2().isEmpty() );

        //no change.. so there must be no address suggestion
        reset(presenter);
        viewModel.setAddress1(geoResult.getAddress1());
        verify(presenter, times(0)).requestAddressSuggestion();

        //so if user updates address1, we fire address suggestion
        reset(presenter);
        viewModel.setAddress1("0 South State");
        verify(presenter).requestAddressSuggestion();

        //same happens if we update address2
        reset(presenter);
        viewModel.setAddress2("Madison, Wisconsin");
        verify(presenter).requestAddressSuggestion();

        presenter.inactive(false);
    }

    @Test
    public void testSavingAddress(){

        String fileLocation = "absolute_path";
        presenter.active("");
        ShortAddress selectedAddress = provider.getSelectedAddress();

        //first item from mAddresses is selected to update selected address
        presenter.requestAddressByGeolocation();
        assertEquals( addresses.get(0).getAddress1(), selectedAddress.getAddress1());

        Response<RouteMessage> response = mock( Response.class );
        presenter.saveAddress(response);

        //we don't provide name, so that is an error saving!!
        verify( response ).onError(any(Exception.class));

        //ok, now lets try to update the commute type.. this should not work!
        reset( provider );
        viewModel.setCommuteType("Z");
        verify(provider, times(0)).updateAddress(any(ShortAddress.class));

        //lets do that then!
        reset( response );
        viewModel.setName("Home");
        presenter.saveAddress(response);

        //fantastic, now this worked!
        verify(response).onResult(any(RouteMessage.class));
        reset( provider );

        //ok now lets update the transportation mode!
        viewModel.setCommuteType(Commute.BICYCLE);
        verify(provider, times(1)).updateAddress(any(ShortAddress.class));

/*
        viewModel.setAvoidTolls(true);
        viewModel.setAvoidXpressway(true);
        verify(provider, times(3)).updateAddress(any(ShortAddress.class));*/
    }

    @Test
    public void testSavingWithCommute(){

        presenter.active("");
        ShortAddress selectedAddress = provider.getSelectedAddress();

        //first item from mAddresses is selected to update selected address
        presenter.requestAddressByGeolocation();
        assertEquals( addresses.get(0).getAddress1(), selectedAddress.getAddress1());

        Response<RouteMessage> response = mock( Response.class );
        presenter.saveAddress(response);

        //we don't provide name, so that is an error saving!!
        verify( response ).onError(any(Exception.class));

        //lets do that then!
        reset( response );
        viewModel.setName("Home");
        presenter.saveAddress(response);

        //fantastic, now this worked!
        verify(response).onResult(any(RouteMessage.class));
    }

    @Test
    public void testSavingAddressWithErrors(){

        String errorCode = "funkyError";

        doAnswer( invocation -> {
            List<SubmitError> errors = new ArrayList<SubmitError>();
            errors.add( new SubmitError(errorCode, "lucky"));
            return errors;
        }).when( provider ).validate(any(ShortAddress.class));

        provider.selectAddress( new ShortAddress());

        Response<RouteMessage> response = mock( Response.class );

        presenter.saveAddress( response );
        verify( response ).onError(any(MapMemoryException.class));

        reset(response);
        when( provider.validate(any(ShortAddress.class)) ).thenReturn( new ArrayList<SubmitError>());

        doAnswer(invocation -> {
            ShortAddress address = invocation.getArgumentAt(0, ShortAddress.class );
            Response<ShortAddress>  thisResponse = invocation.getArgumentAt(1, Response.class );
            thisResponse.onResult( address );
            return null;
        }).when(provider).updateAddressAsync(any(ShortAddress.class), any(Response.class));

        presenter.saveAddress( response );
        verify( response ).onResult(any(RouteMessage.class));
    }

    //<editor-fold desc="utils">
    void applySuccessfulResults(){
        setAddresses();

        doReturn( "resource_string" ).when( addressView ).getString( anyInt() );

        doReturn(true).when(networkServiceMocked).isConnected();

        doAnswer(invocation -> {
            QuickResponse<Boolean> response = invocation.getArgumentAt(0, Response.class);
            response.onResult(true);
            return null;
        }).when( networkServiceMocked ).connect(any(QuickResponse.class));


        doAnswer(invocation -> {
            QuickResponse<Boolean> response = invocation.getArgumentAt(1, QuickResponse.class );
            response.onResult(true);
            return null;
        }).when( addressServiceMocked ).onStart(any(Activity.class), any(QuickResponse.class) );

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
            provider.updateAddress( address );
        }
    }

    private List<ShortAddress> setAddresses(){

        ShortAddress address;
        //lets add an address, and see if addressesView has updated its mAddresses
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
    //</editor-fold>
}
