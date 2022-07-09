package sk.chatty.models;


public class User {
    private int uid;
    private String apiKey;
    private String username;
    private boolean isOnline;

    public User(int uid, String api_key, String username) {
        this.uid = uid;
        this.apiKey = api_key;
        this.username = username;
    }

    public boolean isOnline() {
        return isOnline;
    }
    public void setOnline(boolean online) {
        isOnline = online;
    }

    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getApiKey() {
        return apiKey;
    }
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
