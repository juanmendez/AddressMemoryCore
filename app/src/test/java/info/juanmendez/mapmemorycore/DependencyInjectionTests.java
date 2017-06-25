package info.juanmendez.mapmemorycore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import info.juanmendez.mapmemorycore.vp.TestApp;
import info.juanmendez.mapmemorycore.vp.vpAddresses.TestAddressesView;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DependencyInjectionTests {
    TestApp testApp;

    @Before
    public void before(){
        testApp = new TestApp();
    }

    @Test
    public void testComponentExists(){
        Assert.assertNotNull( TestApp.getComponent() );

        TestApp.getComponent().inject( new TestAddressesView() );
    }
}