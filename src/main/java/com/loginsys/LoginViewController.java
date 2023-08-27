package com.loginsys;

import com.loginsys.exceptions.NonexistentUserException;
import com.loginsys.management.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.stream.Stream;

import static com.loginsys.management.UsersManager.*;

public class LoginViewController {

    @FXML private TextField identity;
    @FXML private PasswordField password;

    @FXML void login() throws IOException {
        String auth = identity.getText();
        String pass = password.getText();

        User result;

        try {
            if (User.EMAIL_CHECKER.matcher(auth).matches()) {
                result = authByEmailAndPassword(auth, pass);
                if (result != null) {
                    currentlyLoggedInUser = result;
                    App.setRoot("user_dashboard");
                } else {
                    new Alert(Alert.AlertType.ERROR, "Wrong password.", ButtonType.OK).showAndWait();
                }
            } else if (User.USERNAME_CHECKER.matcher(auth).matches()) {
                result = authByUsernameAndPassword(auth, pass);
                if (result != null) {
                    currentlyLoggedInUser = result;
                    App.setRoot("user_dashboard");
                } else {
                    new Alert(Alert.AlertType.ERROR, "Wrong password.", ButtonType.OK).showAndWait();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid credentials", ButtonType.OK).showAndWait();
            }



        } catch (NonexistentUserException e) {
            new Alert(
                    Alert.AlertType.ERROR,
                    e.getMessage(),
                    ButtonType.OK
            ).showAndWait();
        }

    }

    @FXML void cancel() {
        Stream.of(identity, password).forEach(
                TextField::clear
        );
    }

    @FXML void signup() throws IOException {
        App.setRoot("signup_view");
    }

}
