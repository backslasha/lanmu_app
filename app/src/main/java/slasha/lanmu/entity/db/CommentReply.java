package slasha.lanmu.entity.db;


import java.time.LocalDateTime;

public class CommentReply {

    private long id;

    private User from;

    private long fromId;

    private Comment comment;

    private long commentId;


    private User to;

    private long toId;

    private String content;

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


    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }


    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
