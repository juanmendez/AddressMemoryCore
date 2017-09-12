package info.juanmendez.mapmemorycore.mamemorycore;

import android.app.Application;

import org.mockito.Mockito;

import info.juanmendez.mapmemorycore.dependencies.AddressProvider;
import info.juanmendez.mapmemorycore.mamemorycore.dependencies.TestAddressProvider;

import static org.powermock.api.mockito.PowerMockito.doAnswer;


/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class TestApp{

    protected Application application;

    public TestApp() {
        application = Mockito.mock( Application.class );

        doAnswer(invocation -> {
            return "Mocked Error Message " + invocation.getArgumentAt(0, Integer.class ).toString();
        }).when( application ).getString( Mockito.anyInt() );
    }

    public Application getApplication() {
        return application;
    }

    public AddressProvider getAddressProvider() {
        return new TestAddressProvider();
    }
}
