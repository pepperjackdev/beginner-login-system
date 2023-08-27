module com.loginsys.login_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.loginsys to javafx.fxml;
    exports com.loginsys;
}