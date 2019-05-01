package slasha.lanmu.entity.card;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class MessageCard {

    public static final int TYPE_TEXT = 0;

    @Expose
    private long id;
    @Expose
    private int type;
    @Expose
    private Date time;
    @Expose
    private String content;
    @Expose
    private UserCard from;
    @Expose
    private UserCard to;
    @Expose
    private int received;

    public int getReceived() {
        return received;
    }

    public void setReceived(int received) {
        this.received = received;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserCard getFrom() {
        return from;
    }

    public void setFrom(UserCard from) {
        this.from = from;
    }

    public UserCard getTo() {
        return to;
    }

    public void setTo(UserCard to) {
        this.to = to;
    }
}
