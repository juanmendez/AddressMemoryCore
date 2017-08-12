package info.juanmendez.mapmemorycore;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import info.juanmendez.mapmemorycore.dependencies.Response;
import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.mamemorycore.TestApp;
import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.utils.ModelUtils;
import info.juanmendez.mapmemorycore.vp.vpAddress.AddressFragment;
import info.juanmendez.mapmemorycore.vp.vpAddress.AddressPresenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;

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

public class TestingWithoutRealm {

    AddressFragment viewMocked;
    AddressPresenter presenter;
    AddressProvider provider;

    @Before
    public void before() throws Exception {
        MapCoreModule.setApp( new TestApp() );

        viewMocked = mock( AddressFragment.class );
        presenter = new AddressPresenter();
        presenter.register(viewMocked);

        //through MVP, get your hands on the presenter, and subsequently get its dagger dependency
        provider = Whitebox.getInternalState( presenter, "addressProvider" );

        //in another function just insert addresses
        insertAddresses( provider );
    }

    @Test
    public void testAddressProvider(){

        assertEquals(provider.countAddresses(), 4);

        provider.deleteAddressAsync(1, new Response<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                assertEquals( provider.countAddresses(), 3 );
            }

            @Override
            public void onError(Exception exception) {

            }
        });
    }

    @Test
    public void testCloningAddresses(){
        ShortAddress firstAddress = provider.getAddress(1);
        assertNotNull( firstAddress );

        ShortAddress cloned = ModelUtils.cloneAddress( firstAddress );

        assertEquals( cloned.getAddressId(), firstAddress.getAddressId() );
        assertEquals( cloned.getAddress1(), firstAddress.getAddress1() );
        assertEquals( cloned.getAddress2(), firstAddress.getAddress2() );
        assertEquals( cloned.getPhotoLocation(), firstAddress.getPhotoLocation() );

        //check for non existing
        assertEquals( cloned.getUrl(), firstAddress.getUrl() );
        assertTrue( "same lat lon", cloned.getLat()==firstAddress.getLat());
    }

    void insertAddresses( AddressProvider provider ){

        ShortAddress address;
        //lets add an address, and see if addressesView has updated its addresses
        address = new ShortAddress(provider.getNextPrimaryKey());
        address.setName( "1");
        address.setAddress1("0 N. State");
        address.setAddress2( "Chicago, 60641" );
        address.setPhotoLocation( "photo.jpg");
        provider.updateAddress( address );

        address = new ShortAddress(provider.getNextPrimaryKey());
        address.setName( "2");
        address.setAddress1("1 N. State");
        address.setAddress2( "Chicago, 60641" );
        provider.updateAddress( address );

        address = new ShortAddress(provider.getNextPrimaryKey());
        address.setName( "3");
        address.setAddress1("2 N. State");
        address.setAddress2( "Chicago, 60641" );
        provider.updateAddress( address );

        address = new ShortAddress(provider.getNextPrimaryKey());
        address.setName( "4");
        address.setAddress1("3 N. State");
        address.setAddress2( "Chicago, 60641" );
        provider.updateAddress( address );
    }

}
