package sk.chatty.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sk.chatty.models.Chat;
import sk.chatty.models.Message;
import sk.chatty.models.User;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@PropertySource("classpath:database.properties")
public class ChatDAO {
    private static Connection connection;

    @Autowired
    private UserDAO userDAO;

    public ChatDAO(@Value("${PG_URL}") String PG_URL,
                   @Value("${PG_USERNAME}") String PG_USERNAME,
                   @Value("${PG_PASSWORD}") String PG_PASSWORD) throws ClassNotFoundException, SQLException {
        if (connection != null)
            return;

        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(PG_URL, PG_USERNAME, PG_PASSWORD);
    }

    /**
     *
     *
     * @param apiKey your apikey
     * @param uid    chat with user
     * @return       Chat
     * @throws SQLException __
     */
    public Chat checkAreYouInChat(String apiKey, int uid) throws SQLException {
        if (userDAO.getMe(apiKey) == null || userDAO.getUser(apiKey, uid) == null)
            return null;

        User me = userDAO.getMe(apiKey);
        User user = userDAO.getUser(apiKey, uid);
        String SQL =
                "SELECT chats.id AS id, chats.name AS name\n" +
                "From (SELECT chats.id AS id, chats.name AS name\n" +
                "      FROM chats\n" +
                "               INNER JOIN messages ON messages.chat_id = chats.id\n" +
                "      WHERE messages.type = 'join_chat' and messages.user_id = ?) AS t1\n" +
                "INNER JOIN messages ON messages.chat_id = t1.id\n" +
                "WHERE messages.type = 'join_chat' and messages.user_id = ?;"; // bad code :( But it works (I think)

        PreparedStatement ps = connection.prepareStatement(SQL);
        ps.setInt(1, me.getId());
        ps.setInt(1, user.getId());
        ResultSet resultSet = ps.executeQuery();

        Chat chat = null;
        while (resultSet.next())
            chat = new Chat(resultSet.getInt("id"), resultSet.getString("name"));
        return chat;
    }

    public Chat createChatWithUser(String apiKey, int uid) throws SQLException {
        if (checkAreYouInChat(apiKey, uid) != null)
            return checkAreYouInChat(apiKey, uid);
        if (userDAO.getMe(apiKey) == null || userDAO.getUser(apiKey, uid) == null)
            return null;

        User user1 = userDAO.getUser(apiKey, uid);
        User user2 = userDAO.getMe(apiKey);
        String SQL;
        PreparedStatement ps;
        ResultSet resultSet;


        SQL = "INSERT INTO chats (name) VALUES (?) RETURNING id;";
        ps = connection.prepareStatement(SQL);
        ps.setString(1, user1.getUsername() + ", " + user2.getUsername());
        resultSet = ps.executeQuery();

        resultSet.next();
        int chatId = resultSet.getInt("id");

        SQL = "INSERT INTO messages (chat_id, user_id, text, time, type) VALUES (?, ?, '', ?, 'join_chat');";
        ps = connection.prepareStatement(SQL);
        ps.setInt(1, chatId);
        ps.setInt(2, user1.getId());
        ps.setLong(3, Instant.now().getEpochSecond());
        ps.executeUpdate();

        ps = connection.prepareStatement(SQL);
        ps.setInt(1, chatId);
        ps.setInt(2, user2.getId());
        ps.setLong(3, Instant.now().getEpochSecond());
        ps.executeUpdate();

        Chat chat = checkAreYouInChat(apiKey, uid);

        return chat;
    }

    public List<Message> getChatMessages(String apiKey, int cid) throws SQLException {
        String SQL =
                "SELECT *\n" +
                "FROM messages\n" +
                "WHERE chat_id = ?;";

        PreparedStatement ps = connection.prepareStatement(SQL);
        ps.setInt(1, cid);
        ResultSet resultSet = ps.executeQuery();

        ArrayList<Message> messages = new ArrayList<>();
        while (resultSet.next())
            messages.add(new Message(
                    resultSet.getInt("id"),
                    resultSet.getInt("chat_id"),
                    resultSet.getInt("user_id"),
                    resultSet.getString("text"),
                    resultSet.getInt("time"),
                    resultSet.getString("type")
            ));
        return messages;
    }
}
