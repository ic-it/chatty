package sk.chatty.models;

/**
 * I've been thinking for a long time about the best way to make a chat room.
 * And I came up with this very interesting solution.
 * I will not make a table with the users of chat rooms, and the first message,
 *      I will write that two people logged into 1 chat room.
 * Let's see what happens XD
 */


public class Message {
    private int id;
    private int chatId;
    private int userId;
    private String text;
    private int time;
    private String type;

    public Message(int id, int chatId, int userId, String text, int time, String type) {
        this.id = id;
        this.chatId = chatId;
        this.userId = userId;
        this.text = text;
        this.time = time;
        this.type = type;
    }

    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
