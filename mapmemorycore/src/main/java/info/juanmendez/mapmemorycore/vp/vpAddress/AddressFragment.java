package info.juanmendez.mapmemorycore.vp.vpAddress;

import java.io.File;
import java.util.List;

import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.vp.FragmentNav;

/**
 * Created by Juan Mendez on 6/26/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface AddressFragment extends FragmentNav {

    void onNetworkStatus( Boolean online );
    void onAddressResult(ShortAddress address);
    void onAddressError(Exception exception );
    void onAddressesSuggested(List<ShortAddress> addresses );

    void onPhotoSelected( File photo );
    String getAction();
}