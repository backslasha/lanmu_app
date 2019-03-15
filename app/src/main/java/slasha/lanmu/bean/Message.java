package slasha.lanmu.bean;

import java.io.Serializable;

public class Message implements Serializable {
    private User from, to;
    private String content;
    private String date;

    public Message(User from, User to, String content, String date) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.date = date;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
