package info.juanmendez.mapmemorycore;

import android.app.Application;

import org.junit.Before;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.List;

import info.juanmendez.mapmemorycore.dependencies.autocomplete.AutocompleteResponse;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AutocompleteService;
import info.juanmendez.mapmemorycore.mamemorycore.TestApp;
import info.juanmendez.mapmemorycore.mamemorycore.dependencies.TestAutocompleteService;
import info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddress.TestAddressView;
import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mockrealm.MockRealm;
import info.juanmendez.mockrealm.models.RealmAnnotation;
import info.juanmendez.mockrealm.test.MockRealmTester;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@PrepareForTest({TestApp.class})
public class ServicesTests extends MockRealmTester {

    @Before
    public void before() throws Exception {
        MockRealm.prepare();
        MockRealm.addAnnotations( RealmAnnotation.build(Address.class).primaryField("addressId"));

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


    }
}
