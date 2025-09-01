module com.javaninjas.careerpathway {
    requires javafx.controls;
    requires javafx.fxml;

    // add in the other project modules in here when trying to run the application
    exports com.javaninjas.careerpathway.app;
    opens com.javaninjas.careerpathway.app to javafx.fxml;

    exports com.javaninjas.careerpathway.controllers;
    opens com.javaninjas.careerpathway.controllers to javafx.fxml;

    opens com.javaninjas.careerpathway.pages to javafx.fxml;
}