package info.juanmendez.mapmemorycore.vp.vpSuggest;

import java.util.List;

import info.juanmendez.mapmemorycore.models.ShortAddress;
import info.juanmendez.mapmemorycore.vp.FragmentNav;

/**
 * Created by Juan Mendez on 8/16/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface SuggestView extends FragmentNav {

    void setPrintedAddress(String addressText );

    String getPrintedAddress();

    //show all addresses matching a query-address from the fragment
    void setSuggestedAddresses(List<ShortAddress> addresses );

    void onError(Exception exception);
}
