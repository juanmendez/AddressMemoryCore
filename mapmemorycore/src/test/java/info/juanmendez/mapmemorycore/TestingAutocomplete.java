package info.juanmendez.mapmemorycore;

import android.app.Application;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.List;

import info.juanmendez.mapmemorycore.dependencies.Navigation;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AutocompleteResponse;
import info.juanmendez.mapmemorycore.dependencies.autocomplete.AutocompleteService;
import info.juanmendez.mapmemorycore.mamemorycore.TestApp;
import info.juanmendez.mapmemorycore.mamemorycore.dependencies.TestAutocompleteService;
import info.juanmendez.mapmemorycore.mamemorycore.vp.vpAddress.TestAddressFragment;
import info.juanmendez.mapmemorycore.modules.MapCoreModule;
import info.juanmendez.mapmemorycore.vp.vpAddress.AddressPresenter;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;


/**
 * Created by Juan Mendez on 7/7/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public class TestingAutocomplete {

    @Before
    public void before() throws Exception {
        MapCoreModule.setApp( new TestApp() );
    }

    @Test
    public void testAutocomplete(){
        AutocompleteService service = new TestAutocompleteService(mock(Application.class));
        AutocompleteResponse response = mock( AutocompleteResponse.class );

        service.suggestAddress("0 n. State", 0, 0, response);
        verify(response).onAddressResults(any(List.class));
    }

    /**
     allow user to enter a new address
     let user to fill in address by current geolocation
        let user enter address manually if offline
        let user pick one address from matching ones based on what she has entered so far
     allow user to edit a previous address
     user can edit address just as it is done above
     enable user to pick a photo or take one for her address
     */
    @Test
    public void textNetwork(){

        TestAddressFragment addressView = new TestAddressFragment();

        AddressPresenter presenter = addressView.getPresenter();

        //we are going to see if user is on edit mode!
        Navigation navigation = (Navigation) Whitebox.getInternalState(presenter, "navigation");


        /**
         * if on edit mode we need to give instructions to the view how to display the content
         * the view must support an online and an offline form.
         */


    }
}
