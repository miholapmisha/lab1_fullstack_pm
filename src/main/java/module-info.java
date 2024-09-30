module com.lab_1.fullstack_pm {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.lab_1.fullstack_pm to javafx.fxml, com.google.gson;
    exports com.lab_1.fullstack_pm;
}