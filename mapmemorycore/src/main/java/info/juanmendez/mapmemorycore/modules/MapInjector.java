package info.juanmendez.mapmemorycore.modules;

import info.juanmendez.mapmemorycore.vp.vpAddress.AddressPresenter;
import info.juanmendez.mapmemorycore.vp.vpAddresses.AddressesPresenter;
import info.juanmendez.mapmemorycore.vp.vpAddresses.AddressesProxy;
import info.juanmendez.mapmemorycore.vp.vpPhoto.PhotoPresenter;
import info.juanmendez.mapmemorycore.vp.vpSuggest.SuggestPresenter;

/**
 * Created by Juan Mendez on 9/12/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface MapInjector {
    void inject( AddressesPresenter addressesPresenter );
    void inject( AddressPresenter addressPresenter );
    void inject( PhotoPresenter photoPresenter );
    void inject( SuggestPresenter suggestPresenter );
    void inject( AddressesProxy addressesProxy );
}
