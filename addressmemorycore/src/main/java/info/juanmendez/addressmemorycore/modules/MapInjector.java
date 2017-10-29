package info.juanmendez.addressmemorycore.modules;

import info.juanmendez.addressmemorycore.vp.AddressesActivityPresenter;
import info.juanmendez.addressmemorycore.vp.vpAddress.AddressPresenter;
import info.juanmendez.addressmemorycore.vp.vpAddresses.AddressesPresenter;
import info.juanmendez.addressmemorycore.vp.vpAddresses.AddressesProxy;
import info.juanmendez.addressmemorycore.vp.vpPhoto.PhotoPresenter;
import info.juanmendez.addressmemorycore.vp.vpSuggest.SuggestPresenter;

/**
 * Created by Juan Mendez on 9/12/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public interface MapInjector {
    void inject( AddressesActivityPresenter addressesActivityPresenter );
    void inject( AddressesPresenter addressesPresenter );
    void inject( AddressPresenter addressPresenter );
    void inject( PhotoPresenter photoPresenter );
    void inject( SuggestPresenter suggestPresenter );
    void inject( AddressesProxy addressesProxy );
}