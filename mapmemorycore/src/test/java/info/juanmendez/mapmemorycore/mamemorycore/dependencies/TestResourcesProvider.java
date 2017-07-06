package info.juanmendez.mapmemorycore.mamemorycore.dependencies;

import info.juanmendez.mapmemorycore.R;
import info.juanmendez.mapmemorycore.dependencies.ResourcesProvider;


/**
 * Created by Juan Mendez on 7/6/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class TestResourcesProvider implements ResourcesProvider {

    @Override
    public String getString(int stringId) {
        if( stringId == R.string.required_field )
            return "Test says field is required!";
        else if( stringId == R.string.address_gone )
            return "Test says address is gone!";

        return null;
    }
}
