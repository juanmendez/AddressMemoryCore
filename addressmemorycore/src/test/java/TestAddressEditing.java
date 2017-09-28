import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.AddressService;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.Response;
import info.juanmendez.addressmemorycore.models.AddressViewModel;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.modules.MapModuleBase;
import info.juanmendez.addressmemorycore.vp.FragmentNav;
import info.juanmendez.addressmemorycore.vp.vpAddress.AddressPresenter;
import info.juanmendez.addressmemorycore.vp.vpAddress.AddressView;
import info.juanmendez.mapmemorycore.addressmemorycore.TestApp;
import info.juanmendez.mapmemorycore.addressmemorycore.module.DaggerMapCoreComponent;
import info.juanmendez.mapmemorycore.addressmemorycore.module.MapCoreModule;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by Juan Mendez on 9/27/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestAddressEditing {
    AddressView addressView;
    AddressPresenter addressPresenter;

    NetworkService networkServiceMocked;
    AddressService addressServiceMocked;
    NavigationService navigationService;
    private AddressProvider provider;
    String navigationTag = "helloNavigation";
    List<ShortAddress> addresses;

    AddressViewModel viewModel;

    @Before
    public void before() throws Exception {

        addresses = getAddresses();
        MapModuleBase.setInjector( DaggerMapCoreComponent.builder().mapCoreModule(new MapCoreModule(new TestApp())).build() );

        addressView = mock( AddressView.class );
        addressPresenter = new AddressPresenter();
        viewModel = addressPresenter.getViewModel(addressView);

        networkServiceMocked = Whitebox.getInternalState(addressPresenter, "networkService");
        addressServiceMocked = Whitebox.getInternalState(addressPresenter, "addressService");
        navigationService = Whitebox.getInternalState(addressPresenter, "navigationService");
        provider = Whitebox.getInternalState( addressPresenter, "addressProvider" );

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
        addressPresenter.inactive(false);
    }

    @Test
    public void testAddressValidation(){
        addressPresenter.active("");
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

        addressPresenter.inactive(false);
    }

    //<editor-fold desc="utils">
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
    //</editor-fold>
}
