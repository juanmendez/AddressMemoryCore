package info.juanmendez.mapmemorycore.mamemorycore;

import android.app.Application;

import org.mockito.Mockito;

import info.juanmendez.mapmemorycore.CoreApp;
import info.juanmendez.mapmemorycore.dependencies.RealmProvider;
import info.juanmendez.mapmemorycore.dependencies.ResourcesProvider;
import info.juanmendez.mapmemorycore.mamemorycore.dependencies.TestRealmProvider;
import info.juanmendez.mapmemorycore.mamemorycore.dependencies.TestResourcesProvider;


/**
 * Created by Juan Mendez on 6/25/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestApp implements CoreApp {

    Application application;

    public TestApp() {
        application = Mockito.mock( Application.class );
    }

    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public RealmProvider getRealmProvider() {
        return new TestRealmProvider(application);
    }

    @Override
    public ResourcesProvider getResourceProvider() {
        return new TestResourcesProvider();
    }
}
