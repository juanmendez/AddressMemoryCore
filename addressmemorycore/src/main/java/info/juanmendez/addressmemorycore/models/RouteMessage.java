package info.juanmendez.addressmemorycore.models;

/**
 * Created by Juan Mendez on 10/21/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 *
 * Route messages provide information to navigate from one view into another along with a route.
 */

public class RouteMessage {
    String location = "";
    String route = "";
    
    public RouteMessage(String location, String route) {
        this.location = location;
        this.route = route;
    }

    public String getLocation() {
        return location;
    }

    public String getRoute() {
        return route;
    }
}
