package slasha.lanmu.entity.card;


import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.Date;
import java.util.List;


public class CommentCard {
    @Expose
    private long id;

    @Expose
    private long postId;

    @Expose
    private UserCard from;

    @Expose
    private long fromId;

    @Expose
    private String content;

    @Expose
    private Date time;


    public List<CommentReplyCard> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentReplyCard> replies) {
        this.replies = replies;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    @Expose
    private List<CommentReplyCard> replies;

    @Expose
    private int replyCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
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
}
