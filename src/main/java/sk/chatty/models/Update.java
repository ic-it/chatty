package sk.chatty.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Update {
    private ArrayList<User> onlineUsers;
    private ArrayList<User> notices;
    private ArrayList<Message> chatUpdates;
    private Chat chat;

    public Update(ArrayList<User> onlineUsers, ArrayList<User> notices, ArrayList<Message> chatUpdates, Chat chat) {
        this.onlineUsers = onlineUsers;
        this.notices = notices;
        this.chatUpdates = chatUpdates;
        this.chat = chat;
    }

    public Update() {
        this.onlineUsers = new ArrayList<>();
        this.notices = new ArrayList<>();
        this.chatUpdates = new ArrayList<>();
        this.chat = null;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public ArrayList<User> getOnlineUsers() {
        return onlineUsers;
    }

    public void setOnlineUsers(ArrayList<User> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    public ArrayList<User> getNotices() {
        return notices;
    }

    public void setNotices(ArrayList<User> notices) {
        this.notices = notices;
    }

    public ArrayList<Message> getChatUpdates() {
        return chatUpdates;
    }

    public void setChatUpdates(ArrayList<Message> chatUpdates) {
        this.chatUpdates = chatUpdates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Update update = (Update) o;
        return Objects.equals(onlineUsers, update.onlineUsers) && Objects.equals(notices, update.notices) && Objects.equals(chatUpdates, update.chatUpdates) && Objects.equals(chat, update.chat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(onlineUsers, notices, chatUpdates, chat);
    }
}
