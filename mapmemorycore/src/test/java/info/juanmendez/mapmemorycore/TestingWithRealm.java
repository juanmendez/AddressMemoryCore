package info.juanmendez.mapmemorycore;

import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;

import java.util.List;

import info.juanmendez.mapmemorycore.dependencies.db.AddressProvider;
import info.juanmendez.mapmemorycore.mamemorycore.TestRealmApp;
import info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddress.TestAddressFragment;
import info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddresses.TestAddressesFragment;
import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.models.AddressFields;
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
 * These tests show how to write final Realm transactions using Mocking-Realm through TDD.
 * We are using Dagger, and our transactions are happening in RealmAddressProvider which is
 * implementing AddressProvider and which is stated as our dependency in Dagger.
 */
@PrepareForTest({TestRealmApp.class})
public class TestingWithRealm extends MockRealmTester {

    @Before
    public void before() throws Exception {

        MockRealm.prepare();
        MockRealm.addAnnotations( RealmAnnotation.build(Address.class)
                .primaryField(AddressFields.ADDRESSID)
                .indexedFields(AddressFields.NAME, AddressFields.DATEUPDATED, AddressFields.TIMESVISITED));

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
        AddressProvider provider = Whitebox.getInternalState( presenter, "addressProvider" );
        assertEquals(provider.countAddresses(), 0);

        address = provider.getAddress(2);
        assertNull( address );

        provider.updateAddress( new Address(1));

        address = provider.getAddress(1);
        address.setName( "testAddressProvider");
        address.setAddress1("0 N. State");
        address.setAddress2( "Chicago, 60641" );

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
        AddressProvider provider;
        Address address;

        /**
         * lets build addressesView
         */
        TestAddressesFragment addressesView = new TestAddressesFragment();
        provider = Whitebox.getInternalState( addressesView.getPresenter(), "addressProvider" );

        addresses = addressesView.getAddresses();

        assertEquals(0, provider.countAddresses());

        //lets add an address, and see if addressesView has updated its addresses
        address = new Address(provider.getNextPrimaryKey());
        address.setName( "testAddressesView1");
        address.setAddress1("0 N. State");
        address.setAddress2( "Chicago, 60641" );

        //we pull provider, and insert new address
        provider.updateAddress( address );

        //ok, lets see..
        assertEquals( provider.countAddresses(), 1 );


        //lets add another address, and see if it has also updated.
        address = new Address(provider.getNextPrimaryKey());
        address.setName( "testAddressesView2");
        address.setAddress1("0 N. State");
        address.setAddress2( "Chicago, 60641" );
        provider.updateAddress( address );
        assertEquals( provider.countAddresses(), 2 );

        //good, good.. now I want to delete the first item..
        provider.deleteAddressAsync(1, () -> {
            //should be just one.
            assertEquals( provider.countAddresses(), 1 );
        }, error -> {

        });

        //lets see rotation..
        addressesView.setActive(false, null);
        addressesView.setActive(true, null );

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
        AddressProvider provider;
        Address address;

        /**
         * lets build addressesView
         */
        TestAddressesFragment addressesView = new TestAddressesFragment();
        provider = Whitebox.getInternalState( addressesView.getPresenter(), "addressProvider" );

        addresses = addressesView.getAddresses();

        insertAddresses( provider );
        assertEquals( provider.countAddresses(), 4 );

        //we are going to select the first address from provider.
        address = provider.getAddress(1);
        provider.selectAddress( address );

    }

    void insertAddresses( AddressProvider provider ){

        Address address;

        //lets add an address, and see if addressesView has updated its addresses
        address = new Address(provider.getNextPrimaryKey());
        address.setName( "1");
        address.setAddress1("0 N. State");
        address.setAddress2( "Chicago, 60641" );
        provider.updateAddress( address );

        address = new Address(provider.getNextPrimaryKey());
        address.setName( "2");
        address.setAddress1("1 N. State");
        address.setAddress2( "Chicago, 60641" );
        provider.updateAddress( address );

        address = new Address(provider.getNextPrimaryKey());
        address.setName( "3");
        address.setAddress1("2 N. State");
        address.setAddress2( "Chicago, 60641" );
        provider.updateAddress( address );

        address = new Address(provider.getNextPrimaryKey());
        address.setName( "4");
        address.setAddress1("3 N. State");
        address.setAddress2( "Chicago, 60641" );
        provider.updateAddress( address );
    }

    @Test
    public void validateAddress(){
        MockRealm.clearData();

        AddressProvider provider;
        Address address;

        /**
         * lets build addressesView
         */
        TestAddressFragment addressView = new TestAddressFragment();
        provider = Whitebox.getInternalState( addressView.getPresenter(), "addressProvider" );


        //lets start inserting an address with errors
        address = new Address();
        address.setAddress1(" ");
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