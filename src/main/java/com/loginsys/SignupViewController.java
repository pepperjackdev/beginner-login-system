package com.loginsys;

import com.loginsys.management.UsersManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.stream.Stream;

public class SignupViewController {

    @FXML TextField email;
    @FXML TextField username;
    @FXML PasswordField password;

    @FXML void signup() throws IOException {
        String email = this.email.getText();
        String username = this.username.getText();
        String password = this.password.getText();

        try {
            UsersManager.register(email, username, password);
            App.setRoot("login_view");
            new Alert(Alert.AlertType.CONFIRMATION, "Successfully registered!", ButtonType.OK).showAndWait();
        }

        catch (RuntimeException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML void cancel() {
        Stream.of(email, username, password).forEach(
                TextField::clear
        );
    }

    @FXML void login() throws IOException {
        App.setRoot("login_view");
    }
}
