package info.juanmendez.mapmemorycore.modules;

import javax.inject.Singleton;

import dagger.Component;
import info.juanmendez.mapmemorycore.vp.vpAddresses.AddressesPresenter;
import info.juanmendez.mapmemorycore.vp.vpAddresses.AddressesView;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
@Singleton
@Component(modules={MapMemoryModule.class})
public interface MapMemoryComponent {
    void inject(AddressesView addressesView );
    void inject(AddressesPresenter addressesPresenter );
}
