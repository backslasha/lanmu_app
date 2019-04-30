package slasha.lanmu.entity.card;


import com.google.gson.annotations.Expose;

import java.util.Date;


public class CommentReplyCard {

    @Expose
    private long id;

    @Expose
    private long fromId;

    @Expose
    private long commentId;

    @Expose
    private long toId;

    @Expose
    private String fromName;

    @Expose
    private String toName;

    @Expose
    private String content;

    @Expose
    private Date time;


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


    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"fromId\":")
                .append(fromId);
        sb.append(",\"commentId\":")
                .append(commentId);
        sb.append(",\"toId\":")
                .append(toId);
        sb.append(",\"fromName\":\"")
                .append(fromName).append('\"');
        sb.append(",\"toName\":\"")
                .append(toName).append('\"');
        sb.append(",\"content\":\"")
                .append(content).append('\"');
        sb.append(",\"time\":\"")
                .append(time).append('\"');
        sb.append('}');
        return sb.toString();
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

}
