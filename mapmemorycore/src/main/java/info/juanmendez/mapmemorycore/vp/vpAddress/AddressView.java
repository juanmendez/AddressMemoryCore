package info.juanmendez.mapmemorycore.vp.vpAddress;

import java.io.File;
import java.util.List;

import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.vp.FragmentNav;

/**
 * Created by Juan Mendez on 6/26/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * This is the contract for fragments to presenters
 */

public interface AddressView extends FragmentNav {

    //notify fragment when online|offline
    void onNetworkStatus( Boolean online );

    //update fragment with the latest address
    void onAddressResult(ShortAddress address);

    //tell fragment of an error messaged wrapped in an exception
    void onAddressError(Exception exception );

    //show all addresses matching a query-address from the fragment
    void onAddressesSuggested(List<ShortAddress> addresses );

    //tell fragment what is the current picture for the address
    void onPhotoSelected( File photo );

    //tell fragment if address can be deleted
    void canDelete(Boolean allow);

    //tell fragment if address can be submitted
    void canSubmit(Boolean allow);
}