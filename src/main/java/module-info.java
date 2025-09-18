module com.javaninjas.careerpathway {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires transitive java.sql;
    requires com.fasterxml.jackson.databind;
    requires bcrypt;

    // add in the other project modules in here when trying to run the application
    exports com.javaninjas.careerpathway.app;
    opens com.javaninjas.careerpathway.app to javafx.fxml;

    exports com.javaninjas.careerpathway.core.components;
    opens com.javaninjas.careerpathway.core.components to javafx.fxml;
    opens com.javaninjas.careerpathway.core.controllers to javafx.fxml;

    exports com.javaninjas.careerpathway.pages.dashboard.controllers;
    opens com.javaninjas.careerpathway.pages.dashboard.controllers to javafx.fxml;

    exports com.javaninjas.careerpathway.core.layouts;
    opens com.javaninjas.careerpathway.core.layouts to javafx.fxml;

    exports com.javaninjas.careerpathway.pages.login.controllers;
    opens com.javaninjas.careerpathway.pages.login.controllers to javafx.fxml;

    exports com.javaninjas.careerpathway.pages;
    opens com.javaninjas.careerpathway.pages to javafx.fxml;

    exports com.javaninjas.careerpathway.pages.quiz.models;
    opens com.javaninjas.careerpathway.pages.quiz.models to javafx.fxml;

    exports com.javaninjas.careerpathway.pages.registration.controllers;
    opens com.javaninjas.careerpathway.pages.registration.controllers to javafx.fxml;

    exports com.javaninjas.careerpathway.pages.quiz;
    opens com.javaninjas.careerpathway.pages.quiz to javafx.fxml;

    exports com.javaninjas.careerpathway.pages.quiz.controllers;
    opens com.javaninjas.careerpathway.pages.quiz.controllers to javafx.fxml;
}
