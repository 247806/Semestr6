module ksr {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires lombok;
    requires stanford.corenlp;

    opens ksr to javafx.fxml;
    exports ksr;
    exports ksr.extraction;
    opens ksr.extraction to javafx.fxml;
    exports ksr.loading;
    opens ksr.loading to javafx.fxml;
}