package info.juanmendez.addressmemorycore.dependencies.cloud;

/**
 * Created by juan on 1/8/18.
 */

public interface AuthService {
    void login();
    void logout();
    boolean isLoggedIn();
    String getUI();
}
