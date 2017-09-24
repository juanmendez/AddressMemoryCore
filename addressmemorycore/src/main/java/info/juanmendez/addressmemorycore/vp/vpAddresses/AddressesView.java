package info.juanmendez.addressmemorycore.vp.vpAddresses;

import java.util.List;

import info.juanmendez.addressmemorycore.models.ShortAddress;
import info.juanmendez.addressmemorycore.vp.FragmentNav;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AddressesView extends FragmentNav {
    void injectAddresses(List<ShortAddress> addresses );
}
