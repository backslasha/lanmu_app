
package slasha.lanmu.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("from")
    @Expose
    private User from;
    @SerializedName("to")
    @Expose
    private User to;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("postId")
    @Expose
    private String postId;

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
