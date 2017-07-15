package info.juanmendez.mapmemorycore.vp.vpAddress;

import info.juanmendez.mapmemorycore.models.Address;
import info.juanmendez.mapmemorycore.vp.FragmentNav;

/**
 * Created by Juan Mendez on 6/26/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AddressFragment extends FragmentNav {

    /**
     * @param address
     * @param online
     */
    void showAddress(Address address, Boolean online);
}
