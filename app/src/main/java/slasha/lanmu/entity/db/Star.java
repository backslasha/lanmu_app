package slasha.lanmu.entity.db;


import java.time.LocalDateTime;

public class Star {

    private long id;

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    private User from;

    private long fromId;

    private long bookId;

    private long score;

    private LocalDateTime time;

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


    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }


    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }


}
