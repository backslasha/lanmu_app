package slasha.lanmu.entity.card;


import com.google.gson.annotations.Expose;

import java.util.Date;
import java.util.List;


public class CommentCard {
    public static final int ORDER_TIME_CLOSEST_FIRST = 0;
    public static final int ORDER_TIME_REMOTEST_FIRST = 1;
    public static final int ORDER_COMMENT_THUMBS_UP_FIRST = 2;
    public static final int ORDER_DEFAULT = ORDER_TIME_CLOSEST_FIRST;

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
    @Expose
    private List<CommentReplyCard> replies;
    @Expose
    private int thumbsUpCount;
    @Expose
    private int replyCount;
    @Expose
    private boolean thumbsUp;

    public boolean getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(boolean thumbsUp) {
        this.thumbsUp = thumbsUp;
    }


    public int getThumbsUpCount() {
        return thumbsUpCount;
    }

    public void setThumbsUpCount(int thumbsUpCount) {
        this.thumbsUpCount = thumbsUpCount;
    }

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"postId\":")
                .append(postId);
        sb.append(",\"from\":")
                .append(from);
        sb.append(",\"fromId\":")
                .append(fromId);
        sb.append(",\"content\":\"")
                .append(content).append('\"');
        sb.append(",\"time\":\"")
                .append(time).append('\"');
        sb.append(",\"replies\":")
                .append(replies);
        sb.append(",\"replyCount\":")
                .append(replyCount);
        sb.append('}');
        return sb.toString();
    }
}
