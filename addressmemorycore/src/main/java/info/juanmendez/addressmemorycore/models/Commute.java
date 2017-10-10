package info.juanmendez.addressmemorycore.models;

import io.realm.RealmObject;


/**
 * Created by Juan Mendez on 10/6/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public class Commute extends RealmObject {
    public static final String DRIVING = "d";
    public static final String BICYCLE = "b";
    public static final String WALKING = "w";
    public static final String BUS = "t";
    public static final String UNDECIDED = "u";

    private String type = UNDECIDED;
    private boolean avoidXpressway = false;
    private boolean avoidTolls = false;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getAvoidXpressway() {
        return avoidXpressway;
    }

    public void setAvoidXpressway(Boolean avoidXpressway) {
        this.avoidXpressway = avoidXpressway;
    }

    public boolean getAvoidTolls() {
        return avoidTolls;
    }

    public void setAvoidTolls(Boolean avoidTolls) {
        this.avoidTolls = avoidTolls;
    }
}
