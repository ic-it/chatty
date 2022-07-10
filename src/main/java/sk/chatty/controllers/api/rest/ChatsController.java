package sk.chatty.controllers.api.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import sk.chatty.dao.ChatDAO;
import sk.chatty.dao.UserDAO;
import sk.chatty.models.Chat;
import sk.chatty.models.Message;
import sk.chatty.models.Update;
import sk.chatty.models.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;


/**
 * Now implementing only a 1 on 1 chat.
 * But in the future, this can be converted to chat between an unlimited number of users
 */

@RequestMapping("/api/v1")
@RestController
public class ChatsController {
    private final ChatDAO chatDAO;
    private final UserDAO userDAO;
    private final ArrayList<Update> updates;

    @Autowired
    public ChatsController(ChatDAO chatDAO, UserDAO userDAO) {
        this.chatDAO = chatDAO;
        this.userDAO = userDAO;
        updates = new ArrayList<>();
    }

    private void addUpdate(Message message) {
        for (Update update: updates)
        {
            if (update.getChat().getId() == message.getChatId())
            {
                update.getChatUpdates().add(message);
            }
        }
    }

    @GetMapping("/joinChatWith")
    public ResponseEntity<Chat> joinChatWith(
            @RequestParam("api_key") String apiKey,
            @RequestParam("uid") int uid) throws SQLException {
        // If key is invalid or user not found
        if (userDAO.getMe(apiKey) == null || userDAO.getUser(apiKey, uid) == null)
            return new ResponseEntity<>(new Chat(-1, null), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(chatDAO.createChatWithUser(apiKey, uid), HttpStatus.OK);
    }

    @GetMapping("/getChatMessages")
    public ResponseEntity<List<Message>> getChatMessages(
            @RequestParam("api_key") String apiKey,
            @RequestParam("cid") int cid) throws SQLException {
        // If key is invalid
        if (userDAO.getMe(apiKey) == null)
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        List<Message> messages = chatDAO.getChatMessages(apiKey, cid);
        // If chat not found
        if (messages == null)
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/sendMessage")
    @PostMapping("/sendMessage")
    public ResponseEntity<Message> sendMessage(
            @RequestParam("api_key") String apiKey,
            @RequestParam("cid") int cid,
            @RequestParam("text") String text) throws SQLException {
        // Max symbols
        if (text.length() > 200 || text.length() < 1)
            return new ResponseEntity<>(new Message(), HttpStatus.BAD_REQUEST);

        // If key is invalid
        if (userDAO.getMe(apiKey) == null)
            return new ResponseEntity<>(new Message(), HttpStatus.BAD_REQUEST);
        Message message = chatDAO.sendMessage(apiKey, cid, text);
        // If chat not found
        if (message == null)
            return new ResponseEntity<>(new Message(), HttpStatus.BAD_REQUEST);

        // Notify updates
        addUpdate(message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/getUpdates")
    public DeferredResult<Update> getUpdates(
            @RequestParam("api_key") String apiKey,
            @RequestParam("cid") int cid) throws SQLException {

        //
        long timeOutInMilliSec = 100_000L;
        Update update = new Update();
        DeferredResult<Update> deferredResult = new DeferredResult<>(timeOutInMilliSec, update);

        User me = userDAO.getMe(apiKey);

        // If key is invalid
        if (me == null) {
            deferredResult.setResult(update);
            return deferredResult;
        }

        // If you are not in chat
        if (chatDAO.getChatMembers(apiKey, cid).stream().filter(user -> user.getId() == me.getId()).findAny().orElse(null) == null){
            deferredResult.setResult(update);
            return deferredResult;
        }

        update.setChat(new Chat(cid, ""));
        updates.add(update);

        CompletableFuture.runAsync(()->{
            try {
                int waitTime = 0;
                while (waitTime < timeOutInMilliSec)
                {
                    TimeUnit.SECONDS.sleep(1);
                    if (!update.getChatUpdates().isEmpty()) {
                        deferredResult.setResult(update);
                        updates.remove(update);
                        break;
                    }
                    waitTime += 3*1000;
                }
            }catch (Exception ignored){
            }
        });
        return deferredResult;
    }

}
