package com.loginsys.management.user;

import static com.loginsys.database.DatabaseManager.*;
import com.loginsys.database.property.Property;

import java.util.regex.Pattern;

public record User(String id) {
    public static final Pattern EMAIL_CHECKER = Pattern.compile("[\\w.]+@(?:[\\w-]+\\.)+[\\w-]{2,4}");
    public static final Pattern USERNAME_CHECKER = Pattern.compile("\\w{3,25}");
    public static final Pattern PASSWORD_CHECKER = Pattern.compile(".{15,128}");

    public String get(Property property) {
        return getPropertyByUserId(id, property);
    }

    public void set(Property property, String value) {
        updatePropertyByUserId(id, property, value);
    }

    public String getEmail() {
        return getPropertyByUserId(id, Property.EMAIL);
    }

    public void setEmail(String email) {
        if (EMAIL_CHECKER.matcher(email).matches()) throw new RuntimeException();
        updateEmailByUserId(id, email);
    }

    public String getUsername() {
        return getPropertyByUserId(id, Property.USERNAME);
    }

    public void setUsername(String username) {
        if (USERNAME_CHECKER.matcher(username).matches()) throw new RuntimeException();
        updateUsernameByUserId(id, username);
    }

    public String getPassword() {
        return getPropertyByUserId(id, Property.PASSWORD);
    }

    public void setPassword(String password) {
        if (PASSWORD_CHECKER.matcher(password).matches()) throw new RuntimeException();
        updatePasswordByUserId(id, password);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        } else if (obj instanceof User user) {
            return user.id().equals(this.id());
        }

        return false;

    }

    @Override
    public String toString() {
        return id;
    }
}
