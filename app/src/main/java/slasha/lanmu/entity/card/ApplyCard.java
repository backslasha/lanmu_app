package slasha.lanmu.entity.card;

import com.google.gson.annotations.Expose;

import java.util.Date;


public class ApplyCard {
    @Expose
    private long id;
    @Expose
    private UserCard from;
    @Expose
    private long fromId;
    @Expose
    private Date time;
    @Expose
    private UserCard to;
    @Expose
    private Long toId;
    @Expose
    private int handle = 0;

    public int getReceived() {
        return received;
    }

    public void setReceived(int received) {
        this.received = received;
    }

    @Expose
    private int received = 0;

    public UserCard getFrom() {
        return from;
    }

    public void setFrom(UserCard from) {
        this.from = from;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public UserCard getTo() {
        return to;
    }

    public void setTo(UserCard to) {
        this.to = to;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }


    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public int getHandle() {
        return handle;
    }

    public void setHandle(int handle) {
        this.handle = handle;
    }
}
