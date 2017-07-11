package info.juanmendez.mapmemorycore;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import info.juanmendez.mapmemorycore.dependencies.db.AddressProvider;
import info.juanmendez.mapmemorycore.mamemorycore.TestApp;
import info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddress.TestAddressView;
import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;

import static org.junit.Assert.assertEquals;

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
 * work in parallel and can be switched while testing. The good news is RealmAddressProvider is set to work
 * with our Android environment.
 */

public class TestingWithoutRealm {

    @Before
    public void before() throws Exception {
        MapCoreModule.setApp( new TestApp() );
    }

    @Test
    public void testAddressProvider(){
        TestAddressView addressView = new TestAddressView();
        addressView.onStart();

        //through MVP, get your hands on the presenter, and subsequently get its dagger dependency
        AddressProvider provider = Whitebox.getInternalState( addressView.getPresenter(), "addressProvider" );

        //in another function just insert addresses
        insertAddresses( provider );

        assertEquals(provider.countAddresses(), 4);

        provider.deleteAddressAsync(1, () -> {
            assertEquals( provider.countAddresses(), 3 );
        }, error -> {});
    }

    void insertAddresses( AddressProvider provider ){

        Address address;
        //lets add an address, and see if addressesView has updated its addresses
        address = new Address(provider.getNextPrimaryKey());
        address.setName( "1");
        address.setAddress("0 N. State");
        address.setCity( "Chicago" );
        address.setZip( "60641" );
        provider.updateAddress( address );

        address = new Address(provider.getNextPrimaryKey());
        address.setName( "2");
        address.setAddress("1 N. State");
        address.setCity( "Chicago" );
        address.setZip( "60641" );
        provider.updateAddress( address );

        address = new Address(provider.getNextPrimaryKey());
        address.setName( "3");
        address.setAddress("2 N. State");
        address.setCity( "Chicago" );
        address.setZip( "60641" );
        provider.updateAddress( address );

        address = new Address(provider.getNextPrimaryKey());
        address.setName( "4");
        address.setAddress("3 N. State");
        address.setCity( "Chicago" );
        address.setZip( "60641" );
        provider.updateAddress( address );
    }

}
