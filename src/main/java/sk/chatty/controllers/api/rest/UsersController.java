package sk.chatty.controllers.api.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.chatty.dao.UserDAO;
import sk.chatty.models.User;

import java.sql.SQLException;
import java.util.ArrayList;


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
     * @return User Ok or BAD_REQUEST
     */
    @GetMapping("/createUser")
    @PostMapping("/createUser")
    public ResponseEntity<User> createNewUser(@RequestParam("username") String username) throws SQLException {
        // max uname length
        if (username.length() > 200 || username.length() < 1)
            return new ResponseEntity<>(new User(), HttpStatus.BAD_REQUEST);

        User user = userDAO.createUser(username);
        if (user == null)
            return new ResponseEntity<>(new User(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Get information about Me
     *
     * @param apiKey api_key
     * @return User Ok or BAD_REQUEST
     */
    @GetMapping("/me")
    public ResponseEntity<User> getMe(@RequestParam("api_key") String apiKey) throws SQLException {
        User user = userDAO.getMe(apiKey);

        // If invalid key
        if (user == null)
            return new ResponseEntity<>(new User(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Change Username
     *
     * @param apiKey api_key
     * @param username new username
     * @return User Ok or BAD_REQUEST
     */
    @GetMapping("/me/change")
    @PostMapping("/me/change")
    public ResponseEntity<User> changeMe(@RequestParam("api_key") String apiKey,
                         @RequestParam("username") String username) throws SQLException {
        // max uname length
        if (username.length() > 200 || username.length() < 1)
            return new ResponseEntity<>(new User(), HttpStatus.BAD_REQUEST);

        User user = userDAO.getMe(apiKey);
        // If invalid key
        if (user == null)
            return new ResponseEntity<>(new User(), HttpStatus.BAD_REQUEST);

        user.setUsername(username);
        userDAO.updateUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Get all users
     *
     * @param apiKey api_key
     * @return User Ok or BAD_REQUEST
     */
    @GetMapping("/getUsers")
    public ResponseEntity<ArrayList<User>> getUsers(@RequestParam("api_key") String apiKey) throws SQLException {
        // If invalid key
        if (userDAO.getMe(apiKey) == null)
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(userDAO.getUsers(apiKey), HttpStatus.OK);
    }


    /**
     * Get user info
     *
     * @param apiKey api_key
     * @param uid user uid
     * @return User Ok or BAD_REQUEST
     */
    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestParam("api_key") String apiKey,
                        @RequestParam("uid") int uid) throws SQLException {
        if (userDAO.getMe(apiKey) == null)
            return new ResponseEntity<>(new User(), HttpStatus.BAD_REQUEST);
        User user = userDAO.getUser(apiKey, uid);

        // If user not found
        if (user == null)
            return new ResponseEntity<>(new User(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
