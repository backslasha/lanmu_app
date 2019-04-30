
package slasha.lanmu.entity.local;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import slasha.lanmu.entity.card.UserCard;

public class Comment implements Serializable {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("from")
    @Expose
    private UserCard from;
    @SerializedName("to")
    @Expose
    private UserCard to;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("post_id")
    @Expose
    private String postId;
//    @SerializedName("replies")
//    @Expose
//    private List<CommentReply> replies;
//    @SerializedName("reply_count")
//    @Expose
//    private int replyCount;

//    public List<CommentReply> getReplies() {
//        return replies;
//    }
//
//    public void setReplies(List<CommentReply> replies) {
//        this.replies = replies;
//    }
//
//    public int getReplyCount() {
//        return replyCount;
//    }
//
//    public void setReplyCount(int replyCount) {
//        this.replyCount = replyCount;
//    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
