
package slasha.lanmu.entity.local;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CommentReply implements Serializable {


    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("comment_id")
    @Expose
    private long commentId;
    @SerializedName("from")
    @Expose
    private User from;
    @SerializedName("to")
    @Expose
    private User to;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("post_id")
    @Expose
    private String postId;

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

}
