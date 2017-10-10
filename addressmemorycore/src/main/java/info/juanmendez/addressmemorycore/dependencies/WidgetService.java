package info.juanmendez.addressmemorycore.dependencies;

/**
 * Created by Juan Mendez on 9/6/2017.
 * www.juanmendez.info
 * contact@juanmendez.info
 */

public interface WidgetService {
    void updateList();
    void updateWidgets(int widgetIdExcluded);
    void refreshTransporationMode();
}