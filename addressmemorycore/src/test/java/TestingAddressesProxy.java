import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.vp.vpAddresses.AddressesProxy;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Juan Mendez on 9/4/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class TestingAddressesProxy extends TestAddressMemoryCore{

    @Before
    public void before() throws Exception {

    }

    @Test
    public void testProxy() throws Exception {
        AddressesProxy proxy = new AddressesProxy(coreModule.getAddressProvider());
        AddressProvider provider = coreModule.getAddressProvider();
        assertNotNull( provider );

        List<ShortAddress> addresses = getAddresses();

        for( ShortAddress address: addresses ){
            provider.updateAddress( address );
        }

        //show these objects were created by realm.
        for( ShortAddress address: provider.getAddresses() ){
            assertTrue( address.isValid() );
        }

        //busted, mAddressProvider updates happen after proxy.refresh() is called..
        //so we make a secret call to make ends meet.
        assertEquals( provider.countAddresses(), proxy.getAddresses().size() );
    }

    private List<ShortAddress> getAddresses(){

        List<ShortAddress> addresses = new ArrayList<>();

        ShortAddress address;
        //lets add an address, and see if addressesView has updated its mAddresses
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

        return addresses;
    }
}