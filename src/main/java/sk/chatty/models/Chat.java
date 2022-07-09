package sk.chatty.models;

public class Chat {
    private int id;
    private String name;

    public Chat(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public Chat() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
