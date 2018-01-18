import android.app.Application;

import org.mockito.Mockito;

import info.juanmendez.addressmemorycore.dependencies.AddressService;
import info.juanmendez.addressmemorycore.dependencies.NavigationService;
import info.juanmendez.addressmemorycore.dependencies.NetworkService;
import info.juanmendez.addressmemorycore.dependencies.PhotoService;
import info.juanmendez.addressmemorycore.dependencies.WidgetService;
import info.juanmendez.addressmemorycore.models.AppConfig;
import info.juanmendez.addressmemorycore.modules.AddressCoreModule;
import info.juanmendez.mapmemorycore.addressmemorycore.dependencies.TestAddressProvider;

import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by Juan Mendez on 11/12/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestAddressMemoryCore {
    protected AddressCoreModule coreModule;

    public TestAddressMemoryCore(){

        Application application = Mockito.mock( Application.class );

        doAnswer(invocation -> {
            return "Mocked Error Message " + invocation.getArgumentAt(0, Integer.class ).toString();
        }).when( application ).getString( Mockito.anyInt() );

        coreModule = new AddressCoreModule();
        coreModule.applyApplication(application)
                .applyAddressProvider( new TestAddressProvider() )
                .applyAddressService( mock( AddressService.class ) )
                .applyNavigationService( mock(NavigationService.class)  )
                .applyNetworkService( mock( NetworkService.class ) )
                .applyPhotoService( mock( PhotoService.class ) )
                .applyWidgetService( mock(WidgetService.class) )
                .applyAppConfig( new AppConfig() );
    }
}