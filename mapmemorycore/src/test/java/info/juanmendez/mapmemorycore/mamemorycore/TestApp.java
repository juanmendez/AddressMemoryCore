package info.juanmendez.mapmemorycore.mamemorycore;

import android.app.Application;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import info.juanmendez.mapmemorycore.CoreApp;
import info.juanmendez.mapmemorycore.dependencies.RealmProvider;
import info.juanmendez.mapmemorycore.mamemorycore.dependencies.TestRealmProvider;


/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestApp implements CoreApp {

    Application application;

    public TestApp() {
        application = Mockito.mock( Application.class );
        PowerMockito.doAnswer(invocation -> {

            return "Mocked Error Message " + invocation.getArgumentAt(0, Integer.class ).toString();
        }).when( application ).getString( Mockito.anyInt() );
    }

    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public RealmProvider getRealmProvider() {
        return new TestRealmProvider(application);
    }
}
