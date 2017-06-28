package info.juanmendez.mapmemorycore;

import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;

import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.mamemorycore.TestApp;
import info.juanmendez.mapmemorycore.mamemorycore.dependencies.TestRealmProvider;
import info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddress.TestAddressView;
import info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddresses.TestAddressesView;
import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.vp.vpAddresses.AddressesPresenter;
import info.juanmendez.mockrealm.MockRealm;
import info.juanmendez.mockrealm.models.RealmAnnotation;
import info.juanmendez.mockrealm.test.MockRealmTester;
import io.realm.RealmResults;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@PrepareForTest({TestRealmProvider.class})
public class DependencyInjectionTests extends MockRealmTester {

    @Before
    public void before() throws Exception {

        MockRealm.prepare();
        MockRealm.addAnnotations( RealmAnnotation.build(Address.class).primaryField("addressId"));

        MapCoreModule.setApp( new TestApp() );
    }

    @Test
    public void testAddressProvider(){

        RealmResults<Address> addresses;
        Address address;

        assertNotNull( MapCoreModule.getComponent() );

        //Injects component above.
        AddressesPresenter presenter = new AddressesPresenter();

        //Pull the injection, and test it!
        AddressProvider provider = Whitebox.getInternalState( presenter, "addressProvider" );

        addresses = provider.getAddresses();
        assertEquals(addresses.size(), 0);

        address = provider.getAddress(2);
        assertNull( address );

        provider.updateAddress( new Address(1));

        address = provider.getAddress(1);
        address.setName( "Home");
        address.setAddress("0 N. State");
        address.setCity( "Chicago" );
        address.setZip( "60641" );

        provider.updateAddressAsync( address, () -> {
            assertTrue( true );
        }, error -> {
            assertTrue( false );
        } );

        provider.deleteAddressAsync( 1, () -> {
            assertTrue( true );
        }, error -> {
            assertTrue( false );
        } );
    }

    /**
     * In this scenario there is an activity or fragment which has a recyclerView, and
     * whose presenter is going to update the list. We are stealing away the provider
     * inside this view's presenter in order to update it and see if the data is changed
     * in the actual view.
     */
    @Test
    public void testAddressesView(){
        MockRealm.clearData();

        RealmResults<Address> addresses;
        AddressProvider provider;
        Address address;

        /**
         * lets build addressesView
         */
        TestAddressesView addressesView = new TestAddressesView();
        provider = Whitebox.getInternalState( addressesView.getPresenter(), "addressProvider" );

        addressesView.onStart();
        addresses = addressesView.getAddresses();
        assertEquals( addresses.size(), 0 );

        //lets add an address, and see if addressesView has updated its addresses
        address = new Address(provider.getNextPrimaryKey());
        address.setName( "Home");
        address.setAddress("0 N. State");
        address.setCity( "Chicago" );
        address.setZip( "60641" );

        //we pull provider, and insert new address
        provider.updateAddress( address );

        //ok, lets see..
        assertEquals( addressesView.getAddresses().size(), 1 );


        //lets add another address, and see if it has also updated.
        address = new Address(provider.getNextPrimaryKey());
        address.setName( "Home");
        address.setAddress("0 N. State");
        address.setCity( "Chicago" );
        address.setZip( "60641" );
        provider.updateAddress( address );
        assertEquals( addressesView.getAddresses().size(), 2 );

        //good, good.. now I want to delete the first item..
        provider.deleteAddressAsync(1, () -> {
            //should be just one.
            assertEquals( addressesView.getAddresses().size(), 1 );
        }, error -> {

        });

        //lets see rotation..
        addressesView.onPause();
        addressesView.onStart();

        assertEquals( addressesView.getAddresses().size(), 1 );

        //we want to make the o
        address = provider.getAddress(2);
        provider.selectAddress( address );
    }

    /**
     * scenario from addressesView we want to select one item and
     * open addressView.
     * @param provider
     */
    @Test
    public void testAddressView(){

        MockRealm.clearData();

        RealmResults<Address> addresses;
        AddressProvider provider;
        Address address;

        /**
         * lets build addressesView
         */
        TestAddressesView addressesView = new TestAddressesView();
        provider = Whitebox.getInternalState( addressesView.getPresenter(), "addressProvider" );

        addressesView.onStart();
        addresses = addressesView.getAddresses();

        insertAddresses( provider );
        assertEquals( addresses.size(), 4 );

        //we are going to select the first address from provider.
        address = provider.getAddress(1);
        provider.selectAddress( address );


        //programmatically we are going to start the next view
        TestAddressView addressView = new TestAddressView();
        addressView.onStart();

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