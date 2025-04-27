module com.example.bonplan {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.mail;
    requires org.apache.pdfbox;
    requires java.desktop;

    opens com.example.bonplan.Entities to javafx.base;
    opens com.example.bonplan to javafx.fxml;
    exports com.example.bonplan;
    exports com.example.bonplan.Controllers;
    opens com.example.bonplan.Controllers to javafx.fxml;
}