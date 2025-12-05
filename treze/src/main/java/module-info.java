module org.example.treze {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.treze to javafx.fxml;
    exports org.example.treze;
    
    opens org.example.treze.controllers to javafx.fxml;
    exports org.example.treze.controllers;
    
    opens org.example.treze.model to javafx.base;
    exports org.example.treze.model;
}