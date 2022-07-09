package sk.chatty.controllers.api.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sk.chatty.dao.ChatDAO;
import sk.chatty.dao.MessageDAO;
import sk.chatty.models.Chat;

import java.sql.SQLException;


/**
 * Now implementing only a 1 on 1 chat.
 * But in the future, this can be converted to chat between an unlimited number of users
 */

@RequestMapping("/api/v1")
@RestController
public class ChatsController {
    private final MessageDAO messageDAO;
    private final ChatDAO chatDAO;

    @Autowired
    public ChatsController(MessageDAO messageDAO, ChatDAO chatDAO) {
        this.messageDAO = messageDAO;
        this.chatDAO = chatDAO;
    }

    @GetMapping("/joinChatWith")
    public Chat joinChatWith(@RequestParam("api_key") String api_key,
                             @RequestParam("uid") int uid) throws SQLException {
        return chatDAO.createChatWithUser(api_key, uid);
    }

//    @GetMapping("/get")
//    public Chat joinChatWith(@RequestParam("api_key") String api_key,
//                             @RequestParam("uid") int uid) throws SQLException {
//        return chatDAO.createChatWithUser(api_key, uid);
//    }

}
