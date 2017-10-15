import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.modules.MapModuleBase;
import info.juanmendez.addressmemorycore.utils.ModelUtils;
import info.juanmendez.addressmemorycore.vp.FragmentNav;
import info.juanmendez.addressmemorycore.vp.vpAddress.AddressPresenter;
import info.juanmendez.addressmemorycore.vp.vpAddresses.AddressesPresenter;
import info.juanmendez.addressmemorycore.vp.vpAddresses.AddressesView;
import info.juanmendez.addressmemorycore.vp.vpAddresses.AddressesViewModel;
import info.juanmendez.mapmemorycore.addressmemorycore.TestApp;
import info.juanmendez.mapmemorycore.addressmemorycore.module.DaggerMapCoreComponent;
import info.juanmendez.mapmemorycore.addressmemorycore.module.MapCoreModule;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * Created by Juan Mendez on 9/27/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class TestAddressesView {
    private AddressesView addressView;
    private AddressesPresenter presenter;
    private NavigationService navigationService;
    private AddressProvider provider;

    private String navigationTag = "helloNavigation";
    private List<ShortAddress> addresses = new ArrayList<>();

    private AddressesViewModel viewModel;

    @Before
    public void before() throws Exception {

        MapModuleBase.setInjector( DaggerMapCoreComponent.builder().mapCoreModule(new MapCoreModule(new TestApp())).build() );

        addressView = mock( AddressesView.class );
        presenter = spy(new AddressesPresenter());

        //we want to spy the viewModel, so we get it, and put it back as a spied one
        viewModel = Whitebox.getInternalState( presenter, "viewModel");

        navigationService = Whitebox.getInternalState(presenter, "navigationService");
        provider = Whitebox.getInternalState(presenter, "addressProvider" );

        //make each mocked object answer with positive results such as networkService.isConnected() returning true.
        applySuccessfulResults();
    }

    @Test
    public void testBoostraping(){

        //lets rotate
        int rotate = 1;

        for( int i =0; i<=rotate; i++){
            presenter.active("");
            presenter.getViewModel(addressView);
            //presenter has to assign to the view the streaming list
            assertFalse( viewModel.getStreamingAddresses().isEmpty() );
            assertNotNull( provider.getSelectedAddress() );
            presenter.inactive(true);
        }
    }

    @Test
    public void testMakingSelection(){
        //provider has been updated elsewhere
        ShortAddress selectedAddress = provider.getAddress(1);
        provider.selectAddress(ModelUtils.cloneAddress(selectedAddress) );

        presenter.active("");
        presenter.getViewModel(addressView);
        //presenter has to assign to the view the streaming list
        assertFalse( viewModel.getStreamingAddresses().isEmpty() );
        assertEquals( viewModel.getSelectedAddress().getAddressId(), selectedAddress.getAddressId() );

        //ok, now the view is going to provide another selected item
        selectedAddress = addresses.get(1);
        viewModel.setSelectedAddress( selectedAddress );

        //that should force the provider to update its selectedAddress
        assertEquals( viewModel.getSelectedAddress().getAddressId(), provider.getSelectedAddress().getAddressId() );

        //in the app then we head to another fragment. See if that's happening
        verify( navigationService ).request( eq(AddressPresenter.ADDRESS_VIEW_TAG) );
        presenter.inactive(true);
    }

    @Test
    public void testMakingNewAddress(){

        ShortAddress selectedAddress = provider.getAddress(1);
        provider.selectAddress(ModelUtils.cloneAddress(selectedAddress) );

        presenter.active("");
        presenter.requestNewAddress();

        //in the app then we head to another fragment. See if that's happening
        verify( navigationService ).request( eq(AddressPresenter.ADDDRESS_EDIT_TAG) );
        assertEquals( provider.getSelectedAddress().getAddressId(), 0 );
        presenter.inactive(true);
    }

    //<editor-fold desc="utils">
    private void applySuccessfulResults(){
        setAddresses();

        doReturn( navigationTag ).when( navigationService ).getNavigationTag(any(FragmentNav.class));

        for(ShortAddress address: addresses ){
            provider.updateAddress( address );
        }
    }

    private void setAddresses(){

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
    }
    //</editor-fold>
}
