package com.loginsys.database;

import com.loginsys.database.property.Property;
import com.loginsys.exceptions.EditingNonEditablePropertyException;
import com.loginsys.exceptions.SearchWithNonSearchablePropertyException;
import com.loginsys.management.user.User;

import java.sql.*;
import java.util.Objects;

public class DatabaseManager {

    /**
     * The connection string. This variable contains the address which points to a SQLite database file.
     * The address is "jdbc:sqlite:users_data.db" where "users_data.db" is the db file.
     */
    private static final String connectionString = "jdbc:sqlite:users_data.db";

    /**
     * Registers a new user to the database.
     *
     * @param id            User's id. It can be omitted.
     * @param email         The email.
     * @param username      The username.
     * @param password      The user password.
     */
    public static void registerNewUserToDatabase(String id, String email, String username, String password) {

        Objects.requireNonNull(id);
        Objects.requireNonNull(email);
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try (Connection connection = DriverManager.getConnection(connectionString)) {
            PreparedStatement stm = connection.prepareStatement("insert into Users (id, email, username, password) values (?,?,?,?)");
            stm.setString(1, id);
            stm.setString(2, email);
            stm.setString(3, username);
            stm.setString(4, password);
            stm.executeUpdate();
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Searches into the DB for a matching {@code filter} value and returns a new {@code User} object pointing to the matched row.
     *
     * @param filterType    The column in which search the {@code filter} value. It can be USER_ID, USER_EMAIL or USER_USERNAME.
     * @param filter        The value to search in the column specified by {@code filterType}.
     * @return              A user object which points to the matched row (record); null if no matching is found.
     * @throws SearchWithNonSearchablePropertyException     If the property's type used as filter is a non-searchable value.
     */
    public static User retrieveUserBy(Property filterType, String filter) throws SearchWithNonSearchablePropertyException {

        Objects.requireNonNull(filterType);
        Objects.requireNonNull(filter);

        if (!filterType.searchable) {
            throw new SearchWithNonSearchablePropertyException("Used a non searchable property as filter: " + filterType.value);
        }

        try (Connection connection = DriverManager.getConnection(connectionString)) {
            PreparedStatement stm = connection.prepareStatement("select id from Users where %s = ?".formatted(filterType.value));
            stm.setString(1, filter);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                return new User(rs.getString("id"));
            }

            else {
                return null;
            }
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Searches into the DB for a matching id and returns a new {@code User} object pointing to the matched row.
     * This method is equivalent to {@code retrieveUserBy(Property.ID, id)}.
     *
     * @param id    The id to search in the DB.
     * @return      A new User object pointing to the matched row (record).
     *
     */
    public static User retrieveUserById(String id) {
        return retrieveUserBy(Property.ID, id);
    }

    /**
     * Searches into the DB for a matching email and returns a new {@code User} object pointing to the matched row.
     * This method is equivalent to {@code retrieveUserBy(Property.EMAIL, email)}.
     *
     * @param email     The email to search in the DB.
     * @return          A new User object pointing to the matched row (record).
     *
     */
    public static User retrieveUserByEmail(String email) {
        return retrieveUserBy(Property.EMAIL, email);
    }

    /**
     * Searches into the DB for a matching username and returns a new {@code User} object pointing to the matched row.
     * This method is equivalent to {@code retrieveUserBy(Property.USERNAME, username)}.
     *
     * @param username      The username to search in the DB.
     * @return              A new User object pointing to the matched row (record).
     *
     */
    public static User retrieveUserByUsername(String username) {
        return retrieveUserBy(Property.USERNAME, username);
    }

    /**
     * Returns a String which represents the content of the row's selected property.
     * This method if primary used by the {@code User} object to retrieve at runtime its fields' values.
     *
     * @param id            The id of the user.
     * @param property      The selected user's property.
     * @return              The user's property value.
     *
     */
    public static String getPropertyByUserId(String id, Property property) {

        Objects.requireNonNull(id);
        Objects.requireNonNull(property);

        try (Connection connection = DriverManager.getConnection(connectionString)) {
            PreparedStatement stm = connection.prepareStatement("select * from Users where id = ?");
            stm.setString(1, id);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                return rs.getString(property.value);
            }

            else {
                return null;
            }
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the specified property with the given value.
     *
     * @param id            The user's id corresponding to the row (record) to modify.
     * @param property      The property to update.
     * @param value         The new property value.
     *
     */
    public static void updatePropertyByUserId(String id, Property property, String value) {

        Objects.requireNonNull(id);
        Objects.requireNonNull(property);
        Objects.requireNonNull(value);

        if (!property.editable) {
            throw new EditingNonEditablePropertyException("Trying to edit a non-editable property: " + property.value);
        }

        try (Connection connection = DriverManager.getConnection(connectionString)) {
            PreparedStatement stm = connection.prepareStatement("update Users set %s = ? where id = ?".formatted(property.value));
            stm.setString(1, value);
            stm.setString(2, id);
            stm.executeUpdate();
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the Email with a new value given as argument.
     *
     * @param id        User's id.
     * @param email     New email.
     */
    public static void updateEmailByUserId(String id, String email) {
        updatePropertyByUserId(id, Property.EMAIL, email);
    }

    /**
     * Updates the Username with a new value given as argument.
     *
     * @param id            User's id.
     * @param username      New username.
     */
    public static void updateUsernameByUserId(String id, String username) {
        updatePropertyByUserId(id, Property.USERNAME, username);
    }

    /**
     * Updates the Password with a new value given as argument.
     *
     * @param id            User's id.
     * @param password      New password.
     */
    public static void updatePasswordByUserId(String id, String password) {
        updatePropertyByUserId(id, Property.PASSWORD, password);
    }

    /**
     * Removes the specified user from the db.
     *
     * @param id    The user's id.
     * @return      True, if the user gets deleted; false if not. (probably because it doesn't exist)
     */
    public static boolean deleteRegisteredUserFromDatabase(String id) {

        Objects.requireNonNull(id);

        try (Connection connection = DriverManager.getConnection(connectionString)) {
            PreparedStatement stm = connection.prepareStatement("delete from Users where id = ?");
            stm.setString(1, id);
            return stm.executeUpdate() > 0;
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
