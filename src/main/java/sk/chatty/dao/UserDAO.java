package sk.chatty.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sk.chatty.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Illia
 */

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@PropertySource("classpath:application.properties")
public class UserDAO {
    private static Connection connection;

    public UserDAO(@Value("${PG_URL}") String PG_URL,
                    @Value("${PG_USERNAME}") String PG_USERNAME,
                    @Value("${PG_PASSWORD}") String PG_PASSWORD) throws ClassNotFoundException, SQLException {
        if (connection != null)
            return;

        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(PG_URL, PG_USERNAME, PG_PASSWORD);
    }

    /**
     * Get all users.
     * As same as at `public User getUser(String apiKey, int id) throws SQLException`,
     * api key is not used yet but in future I cat realize blacklist
     *
     * @param apiKey api key
     * @return Users that are available to you
     */
    public ArrayList<User> getUsers(String apiKey) throws SQLException {
        String SQL = "SELECT * FROM users;";

        PreparedStatement ps = connection.prepareStatement(SQL);
        ResultSet resultSet = ps.executeQuery();

        ArrayList<User> users = new ArrayList<>();
        while (resultSet.next())
            users.add(new User(resultSet.getInt("id"), null,
                    resultSet.getString("username"), resultSet.getBoolean("is_online")));

        return users;
    }

    /**
     * Get user by UID
     *
     * In the future, you can add a black list, which means the data for individual tokens will be displayed differently.
     * So for now, I thought it was possible to pass the unused parameter apiKey.
     *
     * @param apiKey api key
     * @param id userId
     * @return User if it exists or null if the user does not exist or you are blacklisted
     */
    public User getUser(String apiKey, int uid) throws SQLException {
        String SQL = "SELECT * FROM users WHERE id = ?;";

        PreparedStatement ps = connection.prepareStatement(SQL);
        ps.setInt(1, uid);
        ResultSet resultSet = ps.executeQuery();

        User user = null;
        while (resultSet.next())
            user = new User(resultSet.getInt("id"), null,
                    resultSet.getString("username"), resultSet.getBoolean("is_online"));
        return user;
    }

    /**
     * Get user by apiKey
     *
     * @param apiKey api key
     * @return User
     */
    public User getMe(String apiKey) throws SQLException {
        String SQL = "SELECT * FROM users WHERE api_key = ?;";
        PreparedStatement ps = connection.prepareStatement(SQL);
        ps.setString(1, apiKey);
        ResultSet resultSet = ps.executeQuery();

        User user = null;
        while (resultSet.next())
            user = new User(resultSet.getInt("id"), resultSet.getString("api_key"),
                    resultSet.getString("username"), resultSet.getBoolean("is_online"));

        return user;
    }

    /**
     * Create user
     * @param username username
     * @return User
     */
    public User createUser(String username) throws SQLException {
        String SQL = "INSERT INTO users (username, api_key) VALUES (?, ?);";
        String apiKey = UUID.randomUUID().toString();
        PreparedStatement ps = connection.prepareStatement(SQL);
        ps.setString(1, username);
        ps.setString(2, apiKey);
        ps.executeUpdate();
        return getMe(apiKey);
    }

    /**
     * Create user
     * @param user user
     * @return User
     */
    public User updateUser(User user) throws SQLException {
        String SQL = " UPDATE users SET username = ?, is_online = ? WHERE id = ?;";
        PreparedStatement ps = connection.prepareStatement(SQL);
        ps.setString(1, user.getUsername());
        ps.setBoolean(2, user.getIsOnline());
        ps.setInt(3, user.getId());
        ps.executeUpdate();
        return user;
    }
}
