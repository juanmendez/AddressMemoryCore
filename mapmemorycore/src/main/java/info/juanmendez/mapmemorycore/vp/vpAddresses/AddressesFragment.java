package info.juanmendez.mapmemorycore.vp.vpAddresses;

import java.util.List;

import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.vp.FragmentNav;

/**
 * Created by Juan Mendez on 6/24/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AddressesFragment extends FragmentNav {
    void injectAddresses(List<ShortAddress> addresses );
}
