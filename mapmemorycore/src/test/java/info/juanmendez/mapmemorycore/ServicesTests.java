package info.juanmendez.mapmemorycore;

import android.app.Application;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import info.juanmendez.mapmemorycore.dependencies.autocomplete.AutocompleteResponse;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AutocompleteService;
import info.juanmendez.mapmemorycore.mamemorycore.TestApp;
import info.juanmendez.mapmemorycore.mamemorycore.dependencies.TestAutocompleteService;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class ServicesTests{

    @Before
    public void before() throws Exception {
        MapCoreModule.setApp( new TestApp() );
    }

    @Test
    public void testAutocomplete(){
        AutocompleteService service = new TestAutocompleteService(mock(Application.class));
        AutocompleteResponse response = mock( AutocompleteResponse.class );

        service.suggestAddress(response, "0 n. State");
        verify(response).onAddressResults(any(List.class));
    }
}
