package sk.chatty.controllers.api.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sk.chatty.dao.UserDAO;
import sk.chatty.models.User;


/**
 * Actions with user/users
 */

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
    public User createNewUser(@RequestParam("username") String username) {
        User user = userDAO.createUser(username);
        if (user == null)
            return new User(-1, null, null);
        return user;
    }

    /**
     * Get information about Me
     *
     * @param api_key api_key
     * @return Me
     */
    @GetMapping("/me")
    public User getMe(@RequestParam("api_key") String api_key) {
        User user = userDAO.getUser(api_key);
        if (user == null)
            return new User(-1, null, null);
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
                         @RequestParam("username") String username) {
        User user = userDAO.getUser(api_key);

        if (user == null)
            return new User(-1, null, null);

        user.setUsername(username);
        return user;
    }
}
