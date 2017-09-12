package info.juanmendez.mapmemorycore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;

import java.util.List;

import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.dependencies.Response;
import info.juanmendez.mapmemorycore.mamemorycore.TestRealmApp;
import info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddresses.TestAddressesFragment;
import info.juanmendez.mapmemorycore.models.AddressFields;
import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.models.SubmitError;
import info.juanmendez.mapmemorycore.modules.DaggerMapCoreComponent;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.modules.SuperComponent;
import info.juanmendez.mapmemorycore.utils.ModelUtils;
import info.juanmendez.mapmemorycore.vp.vpAddresses.AddressesPresenter;
import info.juanmendez.mapmemorycore.vp.vpAddresses.AddressesView;
import info.juanmendez.mockrealm.MockRealm;
import info.juanmendez.mockrealm.models.RealmAnnotation;
import info.juanmendez.mockrealm.test.MockRealmTester;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

/**
 * These tests show how to write final Realm transactions using Mocking-Realm through TDD.
 * We are using Dagger, and our transactions are happening in DroidAddressProvider which is
 * implementing AddressProvider and which is stated as our dependency in Dagger.
 */
@PrepareForTest({TestRealmApp.class})
public class TestingWithRealm extends MockRealmTester {

    AddressesView viewMocked;
    AddressesPresenter presenter;
    AddressProvider provider;

    @Before
    public void before() throws Exception {

        MockRealm.prepare();
        MockRealm.addAnnotations( RealmAnnotation.build(ShortAddress.class)
                .primaryField(AddressFields.ADDRESSID)
                .indexedFields(AddressFields.NAME, AddressFields.DATEUPDATED, AddressFields.TIMESVISITED));

        SuperComponent.setInjector( DaggerMapCoreComponent.builder().mapCoreModule(new MapCoreModule(new TestRealmApp())).build() );

        viewMocked = PowerMockito.mock( AddressesView.class );
        presenter = new AddressesPresenter();
        presenter.register(viewMocked);

        //Pull the injection, and test it!
        provider = Whitebox.getInternalState( presenter, "addressProvider" );
    }

    @Test
    public void testAddressProvider(){

        MockRealm.clearData();
        RealmResults<ShortAddress> addresses;
        ShortAddress address;

        assertNotNull( SuperComponent.getInjector() );

        assertEquals(provider.countAddresses(), 0);

        address = provider.getAddress(2);
        assertNull( address );

        provider.updateAddress( new ShortAddress(1));
        assertEquals( provider.countAddresses(), 1 );

        address = provider.getAddress(1);
        address = ModelUtils.cloneAddress( address );
        address.setName( "testAddressProvider");
        address.setAddress1("0 N. State");
        address.setAddress2( "Chicago, 60641" );

        provider.updateAddress( address );

        address = provider.getAddress(1);
        assertEquals( address.getName(), "testAddressProvider");
    }

    /**
     * In this scenario there is an activity or fragment which has a recyclerView, and
     * whose addressPresenter is going to update the list. We are stealing away the provider
     * inside this view's addressPresenter in order to update it and see if the data is changed
     * in the actual view.
     */
    @Test
    public void testAddressesView(){
        MockRealm.clearData();

        ShortAddress address;

        assertEquals(0, provider.countAddresses());

        //lets add an address, and see if addressesView has updated its addresses
        address = new ShortAddress(provider.getNextPrimaryKey());
        address.setName( "testAddressesView1");
        address.setAddress1("0 N. State");
        address.setAddress2( "Chicago, 60641" );

        //we pull provider, and insert new address
        provider.updateAddress( address );

        //ok, lets see..
        assertEquals( provider.countAddresses(), 1 );


        //lets add another address, and see if it has also updated.
        address = new ShortAddress(provider.getNextPrimaryKey());
        address.setName( "testAddressesView2");
        address.setAddress1("0 N. State");
        address.setAddress2( "Chicago, 60641" );
        provider.updateAddress( address );
        assertEquals( provider.countAddresses(), 2 );

        //good, good.. now I want to delete the first item..
        provider.deleteAddressAsync(1, new Response<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                assertEquals( provider.countAddresses(), 1 );
            }

            @Override
            public void onError(Exception exception) {

            }
        });
    }

    /**
     * scenario from addressesView we want to select one item and
     * open addressView.
     */
    @Test
    public void testAddressView(){

        MockRealm.clearData();

        List<ShortAddress> addresses;
        ShortAddress address;

        /**
         * lets build addressesView
         */
        TestAddressesFragment addressesView = new TestAddressesFragment();

        insertAddresses( provider );
        assertEquals( provider.countAddresses(), 4 );

        //we are going to select the first address from provider.
        address = provider.getAddress(1);
        provider.selectAddress( address );

    }

    @Test
    public void validateAddress(){
        MockRealm.clearData();
        ShortAddress address;

        //lets start inserting an address with errors
        address = new ShortAddress();
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
        errors = provider.validate( new ShortAddress(2));
        assertTrue( errors.size()==1 );
    }

    /**
     * we want to prevent adding addresses which have the same name like others
     */
    @Test
    public void invalidateExistingAddressName(){
        MockRealm.clearData();
        insertAddresses( provider );

        ShortAddress address;

        //we want to add a new address, with an existing name
        address = new ShortAddress();
        address.setName("address-1");
        address.setAddress1( "00 N. State");
        address.setAddress2( "Chicago, IL 60600");

        List<SubmitError> errors = provider.validate( address );
        assertEquals( errors.size(), 1);

        //we want to update an existing address by updating
        //with a name which is unique
        address = new ShortAddress(1);
        address.setName("address-2017");
        address.setAddress1( "00 N. State");
        address.setAddress2( "Chicago, IL 60600");

        errors = provider.validate( address );
        assertEquals( errors.size(), 0);


        //we want toupdate our existing address
        //with the name of another existing address
        address = new ShortAddress(1);
        address.setName("address-2");
        address.setAddress1( "00 N. State");
        address.setAddress2( "Chicago, IL 60600");

        errors = provider.validate( address );
        assertEquals( errors.size(), 1);
    }

    @Test
    public void testDeletingAddress() {
        MockRealm.clearData();
        insertAddresses(provider);

        ShortAddress firstAddress = provider.getAddress(1);
        Response<Boolean> response = mock( Response.class );
        provider.deleteAddressAsync( firstAddress.getAddressId(), response);

        Mockito.verify(response).onResult(eq(true));

        Mockito.reset( response );
        provider.deleteAddressAsync( 2017, response );
        Mockito.verify(response).onError(any(RealmException.class));

    }

    void insertAddresses( AddressProvider provider ){

        ShortAddress address;

        //lets add an address, and see if addressesView has updated its addresses
        address = new ShortAddress(provider.getNextPrimaryKey());
        address.setName( "address-1");
        address.setAddress1("0 N. State");
        address.setAddress2( "Chicago, 60641" );
        provider.updateAddress( address );

        address = new ShortAddress(provider.getNextPrimaryKey());
        address.setName( "address-2");
        address.setAddress1("1 N. State");
        address.setAddress2( "Chicago, 60641" );
        provider.updateAddress( address );

        address = new ShortAddress(provider.getNextPrimaryKey());
        address.setName( "address-3");
        address.setAddress1("2 N. State");
        address.setAddress2( "Chicago, 60641" );
        provider.updateAddress( address );

        address = new ShortAddress(provider.getNextPrimaryKey());
        address.setName( "address-4");
        address.setAddress1("3 N. State");
        address.setAddress2( "Chicago, 60641" );
        provider.updateAddress( address );
    }
}