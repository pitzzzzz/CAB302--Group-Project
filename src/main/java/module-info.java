module com.javaninjas.careerpathway {
    requires javafx.controls;
    requires javafx.fxml;

    // add in the other project modules in here when trying to run the application
    exports com.javaninjas.careerpathway.app;
    opens com.javaninjas.careerpathway.app to javafx.fxml;

    exports com.javaninjas.careerpathway.core.components;
    opens com.javaninjas.careerpathway.core.components to javafx.fxml;
    opens com.javaninjas.careerpathway.core.controllers to javafx.fxml;

    exports com.javaninjas.careerpathway.core.layouts;
    opens com.javaninjas.careerpathway.core.layouts to javafx.fxml;

    exports com.javaninjas.careerpathway.login.controllers;
    opens com.javaninjas.careerpathway.login.controllers to javafx.fxml;

    exports com.javaninjas.careerpathway.pages;
    opens com.javaninjas.careerpathway.pages to javafx.fxml;

    exports com.javaninjas.careerpathway.quiz.model;
    opens com.javaninjas.careerpathway.quiz.model to javafx.fxml;

    exports com.javaninjas.careerpathway.quiz.controller;
    opens com.javaninjas.careerpathway.quiz.controller to javafx.fxml;

    exports com.javaninjas.careerpathway.registration.controllers;
    opens com.javaninjas.careerpathway.registration.controllers to javafx.fxml;

    exports com.javaninjas.careerpathway.quiz;
    opens com.javaninjas.careerpathway.quiz to javafx.fxml;
}
