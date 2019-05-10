package slasha.lanmu.entity.api.comment;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;

public class CreateReplyModel {

    @Expose
    private long commentId;
    @Expose
    private long toId;
    @Expose
    private long fromId;
    @Expose
    private String content;

    private String toName;

    private String fromName;

    private String commentOwnerName; // for view's showing

    public static boolean check(CreateReplyModel model) {
        return !TextUtils.isEmpty(model.content);
    }

    public String getCommentOwnerName() {
        return commentOwnerName;
    }

    public void setCommentOwnerName(String commentOwnerName) {
        this.commentOwnerName = commentOwnerName;
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

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
