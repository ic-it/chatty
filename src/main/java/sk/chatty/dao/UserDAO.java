package sk.chatty.dao;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.stereotype.Component;
import sk.chatty.models.User;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Illia
 */
@Component
public class UserDAO {
    private static int USERS_COUNT = 0;
    private ArrayList<User> users;

    {
        users = new ArrayList<>();
        users.add(new User(++USERS_COUNT, "testkey1", "Illia"));
        users.add(new User(++USERS_COUNT, "testkey2", "Bob"));
        users.add(new User(++USERS_COUNT, "testkey3", "Tom"));
        users.add(new User(++USERS_COUNT, "testkey4", "Jura"));
        users.add(new User(++USERS_COUNT, "testkey5", "Misha"));
    }

    /**
     * Get all users
     *
     * @param apiKey api key
     * @return Users that are available to you
     */
    public ArrayList<User> getUsers(String apiKey)
    {
        return users;
    }

    /**
     * Get user by UID
     *
     * In the future, you can add a black list, which means the data for individual tokens will be displayed differently.
     * So for now, I thought it was possible to pass the unused parameter apiKey
     *
     * @param apiKey api key
     * @param uid userId
     * @return User if it exists or null if the user does not exist or you are blacklisted
     */
    public User getUser(String apiKey, int uid)
    {
        return users.stream().filter(user -> user.getUid() == uid).findAny().orElse(null);
    }

    /**
     * Get user by apiKey
     *
     * @param apiKey api key
     * @return User
     */
    public User getUser(String apiKey)
    {
        return users.stream().filter(user -> user.getApiKey().equals(apiKey)).findAny().orElse(null);
    }

    /**
     * Create user
     * @param username username
     * @return User
     */
    public User createUser(String username) {
        User newUser = new User(++USERS_COUNT, UUID.randomUUID().toString(), username);
        users.add(newUser);
        return newUser;
    }
}
