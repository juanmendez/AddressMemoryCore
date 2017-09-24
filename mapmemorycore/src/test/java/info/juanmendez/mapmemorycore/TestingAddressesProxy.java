package info.juanmendez.mapmemorycore;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;

import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.mamemorycore.TestApp;
import info.juanmendez.mapmemorycore.mamemorycore.module.DaggerMapCoreComponent;
import info.juanmendez.mapmemorycore.mamemorycore.module.MapCoreModule;
import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.modules.MapModuleBase;
import info.juanmendez.mapmemorycore.vp.vpAddresses.AddressesProxy;

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
        AddressProvider provider = (AddressProvider) Whitebox.getInternalState( proxy, "addressProvider" );
        assertNotNull( provider );

        List<ShortAddress> addresses = getAddresses();

        for( ShortAddress address: addresses ){
            provider.updateAddress( address );
        }

        //show these objects were created by realm.
        for( ShortAddress address: provider.getAddresses() ){
            assertTrue( address.isValid() );
        }

        assertEquals( provider.countAddresses(), proxy.getAddresses().size() );

        //show these objects were not created by realm.
        for( ShortAddress address: proxy.getAddresses() ){
            assertFalse( address.isValid() );
        }
    }

    List<ShortAddress> getAddresses(){

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
