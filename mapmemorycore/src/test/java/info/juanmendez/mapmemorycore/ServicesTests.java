package info.juanmendez.mapmemorycore;

import android.app.Application;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.List;

import info.juanmendez.mapmemorycore.dependencies.autocomplete.AutocompleteResponse;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AutocompleteService;
import info.juanmendez.mapmemorycore.dependencies.db.AddressProvider;
import info.juanmendez.mapmemorycore.mamemorycore.TestApp;
import info.juanmendez.mapmemorycore.mamemorycore.dependencies.TestAutocompleteService;
import info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddress.TestAddressView;
import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mockrealm.test.MockRealmTester;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class ServicesTests extends MockRealmTester {

    @Before
    public void before() throws Exception {
        MapCoreModule.setApp( new TestApp() );
    }

    @Test
    public void testAutocomplete(){
        AutocompleteService service = new TestAutocompleteService(mock(Application.class));
        AutocompleteResponse response = mock( AutocompleteResponse.class );

        service.setHandler(response).suggestAddress("0 n. State", 0l, 0l);
        verify(response).onAddressResults(any(List.class));
    }

    @Test
    public void testPresenterWithAutocomplete(){
        TestAddressView addressView = new TestAddressView();
        addressView.onStart();

        AddressProvider provider = Whitebox.getInternalState( addressView.getPresenter(), "addressProvider" );
        insertAddresses( provider );

        assertEquals(provider.countAddresses(), 4);
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
