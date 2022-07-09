package sk.chatty.models;


public class User {
    private int id;
    private String apiKey;
    private String username;
    private boolean isOnline;

    public User(int id, String api_key, String username) {
        this.id = id;
        this.apiKey = api_key;
        this.username = username;
        this.isOnline = false;
    }

    public User(int id, String api_key, String username, boolean isOnline) {
        this.id = id;
        this.apiKey = api_key;
        this.username = username;
        this.isOnline = isOnline;
    }

    public User() {

    }

    public boolean getIsOnline() {
        return isOnline;
    }
    public void setIsOnline(boolean online) {
        isOnline = online;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
