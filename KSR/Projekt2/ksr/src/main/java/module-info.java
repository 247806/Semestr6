module ksr {
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires lombok;
    requires stanford.corenlp;
    requires com.fasterxml.jackson.databind;
    requires jdk.compiler;
    requires com.jfoenix;
    requires javafx.controls;
    requires org.controlsfx.controls;
    requires spring.data.jpa;
    requires spring.data.commons;
    requires jakarta.persistence;
    requires spring.data.relational;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires org.hibernate.orm.core;
    requires net.bytebuddy;
    requires java.desktop;

    opens ksr.zad2 to spring.core, spring.beans, spring.context, javafx.fxml, org.hibernate.orm.core;
    opens ksr.zad2.model;
    exports ksr.zad2;
    exports ksr.zad2.model;
//    exports ksr;
//    exports ksr.extraction;
//    opens ksr.extraction to javafx.fxml;
//    exports ksr.loading;
//    opens ksr.loading to javafx.fxml;
//    exports ksr.utils;
}