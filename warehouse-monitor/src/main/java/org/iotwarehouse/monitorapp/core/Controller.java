package org.iotwarehouse.monitorapp.core;

import javafx.application.Platform;
import javafx.fxml.Initializable;

public interface Controller  {

    default void registerFXTask(Runnable runnable) {
        Platform.runLater(runnable);
    }

    default void postInitialize() {

    }

    default void destroy() {

    }
}
