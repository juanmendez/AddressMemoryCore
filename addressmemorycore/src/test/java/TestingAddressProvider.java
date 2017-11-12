import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.dependencies.Response;
import info.juanmendez.addressmemorycore.models.Commute;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.utils.AddressUtils;
import info.juanmendez.addressmemorycore.vp.vpAddress.AddressPresenter;
import info.juanmendez.addressmemorycore.vp.vpAddress.AddressView;
import info.juanmendez.addressmemorycore.vp.vpAddress.AddressViewModel;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * Created by Juan Mendez on 7/9/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 * This is a short demo using an AddressProvider which doesn't use Realm.
 * It's indeed a lot quicker to test. Additionally through this AddressProvider flavor we
 * can be figure out the same logic we want to apply in real transactions.
 *
 * I first tested with the realm transactions, and I have to say there is still a bit of lagging
 * but it made use of testing and writing them without using Android at all. So both of these flavor providers
 * work in parallel and can be switched while testing. The good news is DroidAddressProvider is set to work
 * with our Android environment.
 */
public class TestingAddressProvider extends TestAddressMemoryCore{

    private AddressViewModel viewModel;
    private AddressView addressView;
    private AddressPresenter presenter;

    String navigationTag = "helloNavigation";
    AddressProvider mAddressProvider;

    List<ShortAddress> mAddresses = new ArrayList<>();

    @Before
    public void before() throws Exception {
        getAddresses();

        addressView = mock( AddressView.class );
        presenter = spy(new AddressPresenter(m));
        viewModel = presenter.getViewModel(addressView);

        //through MVP, get your hands on the presenter, and subsequently get its dagger dependency
        mAddressProvider = m.getAddressProvider();

        //make each mocked object answer with positive results such as networkService.isConnected() returning true.
        applySuccessfulResults();
    }

    @Test
    public void testAddressProvider(){

        assertEquals(mAddressProvider.countAddresses(), mAddresses.size());

        //ok we are going to deletePhoto the selected element. see if mAddressProvider still accounts for that
        ShortAddress selectedAddress = mAddressProvider.getAddress(1);
        mAddressProvider.selectAddress( selectedAddress );

        mAddressProvider.deleteAddressAsync(1, new Response<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                Assert.assertTrue( result );
                assertEquals( mAddressProvider.countAddresses(), mAddresses.size()-1 );
            }

            @Override
            public void onError(Exception exception) {
                Assert.fail(exception.getMessage());
            }
        });

        assertFalse(mAddressProvider.getSelectedAddress() == selectedAddress );
    }

    @Test
    public void testCloningAddresses(){
        ShortAddress firstAddress = mAddressProvider.getAddress(1);
        assertNotNull( firstAddress );

        firstAddress.getCommute().setType(Commute.BICYCLE);
        ShortAddress cloned = AddressUtils.cloneAddress( firstAddress );

        assertEquals( cloned.getAddressId(), firstAddress.getAddressId() );
        assertEquals( cloned.getAddress1(), firstAddress.getAddress1() );
        assertEquals( cloned.getAddress2(), firstAddress.getAddress2() );
        assertEquals( cloned.getPhotoLocation(), firstAddress.getPhotoLocation() );

        //check for non existing
        assertEquals( cloned.getUrl(), firstAddress.getUrl() );
        assertTrue( "same lat lon", cloned.getLat()==firstAddress.getLat());

        //we want to know if cloning brings the new transportation mode.
        assertEquals( cloned.getCommute().getType(), Commute.BICYCLE );
    }

    @Test
    public void testAsync(){
        mAddressProvider.getAddressAsync(4).subscribe(shortAddress -> {
           assertNotNull(shortAddress);
        });
    }

    //<editor-fold desc="utils">
    void applySuccessfulResults(){

        doReturn( "resource_string" ).when(addressView).getString( anyInt() );
        for(ShortAddress address: mAddresses){
            mAddressProvider.updateAddress( address );
        }
    }

    void getAddresses(){

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