package sk.chatty.controllers.api.rest;

import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sk.chatty.dao.UserDAO;
import sk.chatty.models.User;

import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Actions with user/users
 */

//@Validated
@RequestMapping("/api/v1")
@RestController
public class UsersController {
    private final UserDAO userDAO;

    @Autowired
    public UsersController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Create User
     *
     * @param username User name
     * @return new User
     */
    @GetMapping("/createUser")
    public User createNewUser(@RequestParam("username") String username) throws SQLException {
        User user = userDAO.createUser(username);
        if (user == null)
            return new User();
        return user;
    }

    /**
     * Get information about Me
     *
     * @param api_key api_key
     * @return Me
     */
    @GetMapping("/me")
    public User getMe(@RequestParam("api_key") String api_key) throws SQLException {
        User user = userDAO.getMe(api_key);
        if (user == null)
            return new User();
        return user;
    }

    /**
     * Change Username
     *
     * @param api_key api_key
     * @param username new username
     * @return Me
     */
    @GetMapping("/me/change")
    public User changeMe(@RequestParam("api_key") String api_key,
                         @RequestParam("username") String username) throws SQLException {
        User user = userDAO.getMe(api_key);

        if (user == null)
            return new User();

        user.setUsername(username);
        userDAO.updateUser(user);
        return user;
    }

    /**
     * Get all users
     *
     * @param api_key api_key
     * @return new User
     */
    @GetMapping("/getUsers")
    public ArrayList<User> getUsers(@RequestParam("api_key") String api_key) throws SQLException {
        if (userDAO.getMe(api_key) == null)
            return new ArrayList<>();
        return userDAO.getUsers(api_key);
    }


    /**
     * Get user info
     *
     * @param api_key api_key
     * @param uid user id
     * @return new User
     */
    @GetMapping("/getUser")
    public User getUser(@RequestParam("api_key") String api_key,
                                   @RequestParam("uid") int uid) throws SQLException {
        if (userDAO.getMe(api_key) == null)
            return new User();
        return userDAO.getUser(api_key, uid);
    }

}
