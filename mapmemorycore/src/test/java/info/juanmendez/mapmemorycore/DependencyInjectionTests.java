package info.juanmendez.mapmemorycore;

import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;

import java.util.List;

import info.juanmendez.mapmemorycore.dependencies.db.RealmAddressProvider;
import info.juanmendez.mapmemorycore.mamemorycore.TestRealmApp;
import info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddress.TestAddressView;
import info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddresses.TestAddressesView;
import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.models.SubmitError;
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
@PrepareForTest({TestRealmApp.class})
public class DependencyInjectionTests extends MockRealmTester {

    @Before
    public void before() throws Exception {

        MockRealm.prepare();
        MockRealm.addAnnotations( RealmAnnotation.build(Address.class).primaryField("addressId"));

        MapCoreModule.setApp( new TestRealmApp() );
    }

    @Test
    public void testAddressProvider(){

        MockRealm.clearData();
        RealmResults<Address> addresses;
        Address address;

        assertNotNull( MapCoreModule.getComponent() );

        //Injects component above.
        AddressesPresenter presenter = new AddressesPresenter();

        //Pull the injection, and test it!
        RealmAddressProvider provider = Whitebox.getInternalState( presenter, "addressProvider" );
        assertEquals(provider.countAddresses(), 0);

        address = provider.getAddress(2);
        assertNull( address );

        provider.updateAddress( new Address(1));

        address = provider.getAddress(1);
        address.setName( "testAddressProvider");
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

        List<Address> addresses;
        RealmAddressProvider provider;
        Address address;

        /**
         * lets build addressesView
         */
        TestAddressesView addressesView = new TestAddressesView();
        provider = Whitebox.getInternalState( addressesView.getPresenter(), "addressProvider" );

        addressesView.onStart();
        addresses = addressesView.getAddresses();

        assertEquals(0, provider.countAddresses());

        //lets add an address, and see if addressesView has updated its addresses
        address = new Address(provider.getNextPrimaryKey());
        address.setName( "testAddressesView1");
        address.setAddress("0 N. State");
        address.setCity( "Chicago" );
        address.setZip( "60641" );

        //we pull provider, and insert new address
        provider.updateAddress( address );

        //ok, lets see..
        assertEquals( provider.countAddresses(), 1 );


        //lets add another address, and see if it has also updated.
        address = new Address(provider.getNextPrimaryKey());
        address.setName( "testAddressesView2");
        address.setAddress("0 N. State");
        address.setCity( "Chicago" );
        address.setZip( "60641" );
        provider.updateAddress( address );
        assertEquals( provider.countAddresses(), 2 );

        //good, good.. now I want to delete the first item..
        provider.deleteAddressAsync(1, () -> {
            //should be just one.
            assertEquals( provider.countAddresses(), 1 );
        }, error -> {

        });

        //lets see rotation..
        addressesView.onPause();
        addressesView.onStart();

        assertEquals( provider.countAddresses(), 1 );

        //we want to make the o
        address = provider.getAddress(2);
        provider.selectAddress( address );
    }

    /**
     * scenario from addressesView we want to select one item and
     * open addressView.
     */
    @Test
    public void testAddressView(){

        MockRealm.clearData();

        List<Address> addresses;
        RealmAddressProvider provider;
        Address address;

        /**
         * lets build addressesView
         */
        TestAddressesView addressesView = new TestAddressesView();
        provider = Whitebox.getInternalState( addressesView.getPresenter(), "addressProvider" );

        addressesView.onStart();
        addresses = addressesView.getAddresses();

        insertAddresses( provider );
        assertEquals( provider.countAddresses(), 4 );

        //we are going to select the first address from provider.
        address = provider.getAddress(1);
        provider.selectAddress( address );


        //programmatically we are going to start the next view
        TestAddressView addressView = new TestAddressView();
        addressView.onStart();

    }

    void insertAddresses( RealmAddressProvider provider ){

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

    @Test
    public void validateAddress(){
        MockRealm.clearData();

        RealmAddressProvider provider;
        Address address;

        /**
         * lets build addressesView
         */
        TestAddressView addressView = new TestAddressView();
        provider = Whitebox.getInternalState( addressView.getPresenter(), "addressProvider" );


        //lets start inserting an address with errors
        address = new Address();
        address.setAddress(" ");
        List<SubmitError> errors = provider.validate( address);

        assertTrue( errors.size()==3 );

        //how about submitting lat lon instead of address and city.
        address.setLat( 12 );
        address.setLat( 12 );
        errors = provider.validate( address);

        //address and city are not checked, but only name.
        assertTrue( errors.size()==1 );

        /**
         * what happens if we try to validate an address whose id
         * is not in the db.
         */
        errors = provider.validate( new Address(2));
        assertTrue( errors.size()==1 );
    }
}