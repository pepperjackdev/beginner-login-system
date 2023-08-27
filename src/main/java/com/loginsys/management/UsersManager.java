package com.loginsys.management;

import static com.loginsys.database.DatabaseManager.*;

import com.loginsys.exceptions.*;
import com.loginsys.management.user.User;

import java.util.Objects;
import java.util.UUID;

public class UsersManager {

    public static User currentlyLoggedInUser = null;

    public static void register(String email, String username, String password) {
        if (!User.EMAIL_CHECKER.matcher(email).matches())        throw new InvalidUserEmailException("Invalid email.");
        if (!User.USERNAME_CHECKER.matcher(username).matches())  throw new InvalidUserUsernameException("Invalid username.");
        if (!User.PASSWORD_CHECKER.matcher(password).matches())  throw new InvalidUserPasswordException("Invalid password.");

        if (retrieveUserByEmail(email) != null)               throw new RuntimeException("This email is already linked with an another account.");
        if (retrieveUserByUsername(username) != null)         throw new AlreadyTakenUserUsername("Already taken username");

        registerNewUserToDatabase(
                UUID.randomUUID().toString(),
                email,
                username,
                password
        );
    }

    public static User authByEmailAndPassword(String email, String password) {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);

        User user = retrieveUserByEmail(email);

        if (user != null) {
            return (user.getPassword().equals(password)) ? user : null;
        }

        else {
            throw new NonexistentUserException("Non registered email: User not found.");
        }
    }

    public static User authByUsernameAndPassword(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        User user = retrieveUserByUsername(username);

        if (user != null) {
            return (user.getPassword().equals(password)) ? user : null;
        }

        else {
            throw new NonexistentUserException("Non registered username: User not found");
        }
    }

    public static boolean delete(String id) {
        return deleteRegisteredUserFromDatabase(id);
    }

}
