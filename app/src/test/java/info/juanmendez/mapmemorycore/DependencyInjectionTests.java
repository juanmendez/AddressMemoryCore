package info.juanmendez.mapmemorycore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.services.TestAddressProvider;
import info.juanmendez.mapmemorycore.vp.vpAddresses.AddressesPresenter;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DependencyInjectionTests {

    @Before
    public void before(){
        MapCoreModule.setApp( new TestApp() );
    }

    @Test
    public void testComponentExists(){
        Assert.assertNotNull( MapCoreModule.getComponent() );

        AddressesPresenter presenter = new AddressesPresenter();
        Assert.assertNotNull( presenter.getProvider() );
    }

    @Test
    public void testCreateProvider(){
        TestAddressProvider tp = new TestAddressProvider();

        tp.getAddresses(new DisposableSubscriber<List<Address>>() {
            @Override
            public void onNext(List<Address> addresses) {
                Assert.assertTrue( addresses.size() > 0 );
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });

        tp.getAddress(1, new DisposableSubscriber<Address>() {
            @Override
            public void onNext(Address address) {
                Assert.assertNotNull( address );
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}