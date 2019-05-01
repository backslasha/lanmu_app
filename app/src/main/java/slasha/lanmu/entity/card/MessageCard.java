package slasha.lanmu.entity.card;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class MessageCard {

    @Expose
    private long id;
    @Expose
    private int type;
    @Expose
    private Date time;
    @Expose
    private String content;
    @Expose
    private long fromId;
    @Expose
    private long toId;

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

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

}
