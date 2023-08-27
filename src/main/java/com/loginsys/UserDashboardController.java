package com.loginsys;

import com.loginsys.database.property.Property;
import com.loginsys.management.UsersManager;
import com.loginsys.management.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.stream.Stream;

import static com.loginsys.database.DatabaseManager.retrieveUserBy;
import static com.loginsys.management.UsersManager.currentlyLoggedInUser;

public class UserDashboardController {

    @FXML TextField email;
    @FXML TextField username;
    @FXML PasswordField oldPassword;
    @FXML PasswordField newPassword;
    @FXML PasswordField confirmNewPassword;

    @FXML void initialize() {
        email.setText(currentlyLoggedInUser.getEmail());
        username.setText(currentlyLoggedInUser.getUsername());
    }

    void updateProperty(Property property, String updated) {
        String current = currentlyLoggedInUser.get(property);
        if (current.equals(updated)) {
            new Alert(
                    Alert.AlertType.INFORMATION,
                    "Nothing changed.",
                    ButtonType.OK
            ).showAndWait();
        }

        else if (property.searchable && retrieveUserBy(property, updated) != null) {
            new Alert(
                    Alert.AlertType.ERROR,
                    "Already taken " + property.value,
                    ButtonType.OK
            ).showAndWait();
        }

        else {
            try {
                currentlyLoggedInUser.set(property, updated);
                new Alert(Alert.AlertType.CONFIRMATION, "Successfully updated your " + property.value, ButtonType.OK).showAndWait();
                Stream.of(oldPassword, newPassword, confirmNewPassword).forEach(TextField::clear);
            } catch (RuntimeException e) {
                new Alert(
                        Alert.AlertType.ERROR,
                        e.getMessage(),
                        ButtonType.OK
                ).showAndWait();
            }
        }
    }

    @FXML void updateEmailAddress() {

        if (!User.EMAIL_CHECKER.matcher(email.getText()).matches()) {
            new Alert(Alert.AlertType.ERROR, "Invalid email", ButtonType.OK).showAndWait();
            return;
        }

        updateProperty(
                Property.EMAIL,
                email.getText()
        );
    }

    @FXML void updateUsername() {

        if (!User.USERNAME_CHECKER.matcher(username.getText()).matches()) {
            new Alert(Alert.AlertType.ERROR, "Invalid username", ButtonType.OK).showAndWait();
            return;
        }

        updateProperty(
                Property.USERNAME,
                username.getText()
        );
    }

    @FXML void updatePassword() {
        var old = oldPassword.getText();
        var updated = newPassword.getText();
        var confirmed = confirmNewPassword.getText();

        if (old.equals(currentlyLoggedInUser.getPassword())) {
            if (User.PASSWORD_CHECKER.matcher(updated).matches()) {
                if (updated.equals(confirmed)) {
                    updateProperty(Property.PASSWORD, updated);
                } else {
                    new Alert(Alert.AlertType.ERROR, "Your new password doesn't matches its confirmation.", ButtonType.OK).showAndWait();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Your entered an invalid password.", ButtonType.OK).showAndWait();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Your current password (old password field) is wrong. Try again.", ButtonType.OK).showAndWait();
        }

    }

    @FXML void logout() throws IOException {
        currentlyLoggedInUser = null;
        App.setRoot("login_view");
    }

    @FXML void deleteAccount() {
        Alert alert = new Alert(
                Alert.AlertType.WARNING,
                "Danger, by deleting your account all your DATA will be removed! This is not a reversible action. Do you want to proceed?",
                ButtonType.NO,
                ButtonType.YES
        );

        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                UsersManager.delete(currentlyLoggedInUser.id());
                try {
                    logout();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
