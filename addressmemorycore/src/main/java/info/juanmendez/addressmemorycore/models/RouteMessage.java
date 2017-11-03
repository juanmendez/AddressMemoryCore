package info.juanmendez.addressmemorycore.models;

/**
 * Created by Juan Mendez on 10/21/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * Route messages provide information to navigate from one view into another along with a mRoute.
 */

public class RouteMessage {
    String mLocation = "";
    String mRoute = "";
    
    public RouteMessage(String location, String route) {
        mLocation = location;
        mRoute = route;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getRoute() {
        return mRoute;
    }
}
