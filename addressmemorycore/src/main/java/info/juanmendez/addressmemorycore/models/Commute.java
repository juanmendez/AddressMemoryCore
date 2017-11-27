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

    private String mType = DRIVING;
    private boolean mAvoidXpressway = false;
    private boolean mAvoidTolls = false;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public boolean getAvoidXpressway() {
        return mAvoidXpressway;
    }

    public void setAvoidXpressway(Boolean avoidXpressway) {
        mAvoidXpressway = avoidXpressway;
    }

    public boolean getAvoidTolls() {
        return mAvoidTolls;
    }

    public void setAvoidTolls(Boolean avoidTolls) {
        mAvoidTolls = avoidTolls;
    }
}
