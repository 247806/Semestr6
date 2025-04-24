module ksr {
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires lombok;
    requires stanford.corenlp;
    requires com.fasterxml.jackson.databind;
    requires commons.lang3;
    requires jdk.compiler;
    requires com.jfoenix;
    requires javafx.controls;
    requires org.controlsfx.controls;

    opens ksr to javafx.fxml;
    exports ksr;
    exports ksr.extraction;
    opens ksr.extraction to javafx.fxml;
    exports ksr.loading;
    opens ksr.loading to javafx.fxml;
    exports ksr.utils;
}