package info.juanmendez.addressmemorycore.vp.vpAddress;

import info.juanmendez.addressmemorycore.dependencies.QuickResponse;
import info.juanmendez.addressmemorycore.vp.FragmentNav;

/**
 * Created by Juan Mendez on 6/26/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */
public interface AddressView extends FragmentNav {
    void iToast(String message );
    void eToast(String message );
    void checkLocationPermission(QuickResponse<Boolean> response );
}