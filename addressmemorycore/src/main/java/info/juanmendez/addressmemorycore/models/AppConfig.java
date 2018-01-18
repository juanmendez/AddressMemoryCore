package info.juanmendez.addressmemorycore.models;

/**
 * Created by juan on 1/18/18.
 */

public class AppConfig {
    public static String LITE = "addressMemoryLite";
    public static String PRO = "addressMemoryPro";

    private String mFlavor = "";
    private int mAddressLimit = Integer.MAX_VALUE;

    public String getFlavor() {
        return mFlavor;
    }

    public void setFlavor(String flavor) {
        mFlavor = flavor;
    }

    public int getAddressLimit() {
        return mAddressLimit;
    }

    public void setAddressLimit(int addressLimit) {
        this.mAddressLimit = addressLimit;
    }
}
