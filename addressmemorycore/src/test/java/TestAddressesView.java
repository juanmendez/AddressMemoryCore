import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.models.AppConfig;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.utils.AddressUtils;
import info.juanmendez.addressmemorycore.vp.FragmentNav;
import info.juanmendez.addressmemorycore.vp.vpAddress.AddressPresenter;
import info.juanmendez.addressmemorycore.vp.vpAddresses.AddressesPresenter;
import info.juanmendez.addressmemorycore.vp.vpAddresses.AddressesView;
import info.juanmendez.addressmemorycore.vp.vpAddresses.AddressesViewModel;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * Created by Juan Mendez on 9/27/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class TestAddressesView extends TestAddressMemoryCore{
    private AddressesView mAddressView;
    private AddressesPresenter mPresenter;
    private NavigationService mNavigationService;
    private AddressProvider mProvider;

    private String mNavigationTag = "helloNavigation";
    private List<ShortAddress> mAddresses = new ArrayList<>();

    private AddressesViewModel mViewModel;

    @Before
    public void before() throws Exception {

        mAddressView = mock( AddressesView.class );
        mPresenter = spy(new AddressesPresenter(coreModule));

        mViewModel = mPresenter.getViewModel( mAddressView );

        mNavigationService = coreModule.getNavigationService();
        mProvider = coreModule.getAddressProvider();

        //make each mocked object answer with positive results such as networkService.isConnected() returning true.
        applySuccessfulResults();
    }

    @Test
    public void testBoostraping(){

        //lets rotate
        int rotate = 1;

        for( int i =0; i<=rotate; i++){
            mPresenter.active("");
            mPresenter.getViewModel(mAddressView);
            //mPresenter has to assign to the view the streaming list
            assertFalse( mViewModel.getStreamingAddresses().isEmpty() );
            assertNotNull( mProvider.getSelectedAddress() );
            mPresenter.inactive(true);
        }
    }

    @Test
    public void testMakingSelection(){
        //mProvider has been updated elsewhere
        ShortAddress selectedAddress = mProvider.getAddress(1);
        mProvider.selectAddress(AddressUtils.cloneAddress(selectedAddress) );

        mPresenter.active("");
        mPresenter.getViewModel(mAddressView);
        //mPresenter has to assign to the view the streaming list
        assertFalse( mViewModel.getStreamingAddresses().isEmpty() );
        assertEquals( mViewModel.getSelectedAddress().getAddressId(), selectedAddress.getAddressId() );

        //ok, now the view is going to provide another selected item
        selectedAddress = mAddresses.get(1);
        mViewModel.setSelectedAddress( selectedAddress );

        //that should force the mProvider to update its selectedAddress
        assertEquals( mViewModel.getSelectedAddress().getAddressId(), mProvider.getSelectedAddress().getAddressId() );

        //in the app then we head to another fragment. See if that's happening
        verify(mNavigationService).request( eq(AddressPresenter.ADDRESS_VIEW_TAG), eq(Long.toString(selectedAddress.getAddressId())) );
        mPresenter.inactive(true);
    }

    @Test
    public void testMakingNewAddress(){

        ShortAddress selectedAddress = mProvider.getAddress(1);
        mProvider.selectAddress(AddressUtils.cloneAddress(selectedAddress) );

        mPresenter.active("");
        mPresenter.requestNewAddress();

        //in the app then we head to another fragment. See if that's happening
        verify(mNavigationService).request( eq(AddressPresenter.ADDDRESS_EDIT_TAG) );
        assertEquals( mProvider.getSelectedAddress().getAddressId(), 0 );
        mPresenter.inactive(true);
    }

    @Test
    public void testFreeAddressLimit(){
        coreModule.getAppConfig().setFlavor( AppConfig.LITE );
        coreModule.getAppConfig().setAddressLimit( mAddresses.size() );

        mPresenter.active(null);

        //going above
        mPresenter.requestNewAddress();
        verify( mAddressView, times(1) ).notifyAddressLimit();

        //going below
        coreModule.getAppConfig().setAddressLimit( mAddresses.size() + 1 );
        mPresenter.requestNewAddress();
        verify( mAddressView, times(1) ).notifyAddressLimit();


        //using pro flavor
        coreModule.getAppConfig().setFlavor( AppConfig.PRO);
        coreModule.getAppConfig().setAddressLimit( 1 );
        mPresenter.requestNewAddress();
        verify( mAddressView, times(1) ).notifyAddressLimit();
    }

    //<editor-fold desc="utils">
    private void applySuccessfulResults(){
        setAddresses();

        doReturn( "resource_string" ).when(mAddressView).getString( anyInt() );
        doReturn(mNavigationTag).when(mNavigationService).getNavigationTag(any(FragmentNav.class));

        for(ShortAddress address: mAddresses){
            mProvider.updateAddress( address );
        }
    }

    private void setAddresses(){

        ShortAddress address;
        //lets add an address, and see if addressesView has updated its mAddresses
        address = new ShortAddress();
        address.setName( "1");
        address.setAddress1("0 N. State");
        address.setAddress2( "Chicago, 60641" );
        mAddresses.add( address );

        address = new ShortAddress();
        address.setName( "2");
        address.setAddress1("1 N. State");
        address.setAddress2( "Chicago, 60641" );
        mAddresses.add( address );

        address = new ShortAddress();
        address.setName( "3");
        address.setAddress1("2 N. State");
        address.setAddress2( "Chicago, 60641" );
        mAddresses.add( address );

        address = new ShortAddress();
        address.setName( "4");
        address.setAddress1("3 N. State");
        address.setAddress2( "Chicago, 60641" );
        mAddresses.add( address );
    }
    //</editor-fold>
}
