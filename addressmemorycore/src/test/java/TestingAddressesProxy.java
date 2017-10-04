import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.addressmemorycore.dependencies.AddressProvider;
import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.modules.MapModuleBase;
import info.juanmendez.addressmemorycore.vp.vpAddresses.AddressesProxy;
import info.juanmendez.mapmemorycore.addressmemorycore.TestApp;
import info.juanmendez.mapmemorycore.addressmemorycore.module.DaggerMapCoreComponent;
import info.juanmendez.mapmemorycore.addressmemorycore.module.MapCoreModule;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Juan Mendez on 9/4/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class TestingAddressesProxy{

    @Before
    public void before() throws Exception {
        MapModuleBase.setInjector( DaggerMapCoreComponent.builder().mapCoreModule(new MapCoreModule(new TestApp())).build() );
    }

    @Test
    public void testProxy(){
        AddressesProxy proxy = new AddressesProxy();
        AddressProvider provider = Whitebox.getInternalState( proxy, "addressProvider" );
        assertNotNull( provider );

        List<ShortAddress> addresses = getAddresses();

        for( ShortAddress address: addresses ){
            provider.updateAddress( address );
        }

        //show these objects were created by realm.
        for( ShortAddress address: provider.getAddresses() ){
            assertTrue( address.isValid() );
        }

        proxy.refresh();
        assertEquals( provider.countAddresses(), proxy.getAddresses().size() );

        //show these objects were not created by realm.
        long id;
        ShortAddress currentAddress;

        for( int i = 0; i < proxy.getAddresses().size(); i++ ){
            currentAddress = proxy.getAddresses().get(i);
            id = currentAddress.getAddressId();
            assertFalse( currentAddress == provider.getAddress(id));
        }
    }

    private List<ShortAddress> getAddresses(){

        List<ShortAddress> addresses = new ArrayList<>();

        ShortAddress address;
        //lets add an address, and see if addressesView has updated its addresses
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