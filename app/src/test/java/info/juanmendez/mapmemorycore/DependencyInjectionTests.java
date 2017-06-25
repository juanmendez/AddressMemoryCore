package info.juanmendez.mapmemorycore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.vp.vpAddresses.AddressesPresenter;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DependencyInjectionTests {


    @Before
    public void before(){

    }

    @Test
    public void testComponentExists(){
        Assert.assertNotNull( MapCoreModule.getComponent() );

        AddressesPresenter presenter = new AddressesPresenter();
        Assert.assertNotNull( presenter.getProvider() );
    }
}