module com.example.proiectiss {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.logging.log4j;
    requires spring.core;
    requires java.desktop;

    opens com.example.proiectiss to javafx.fxml;
    opens com.example.proiectiss.controller to javafx.fxml;
    opens com.example.proiectiss.repository to javafx.fxml;
    opens com.example.proiectiss.service to javafx.fxml;
    opens com.example.proiectiss.domain to javafx.base;
    exports com.example.proiectiss;
}