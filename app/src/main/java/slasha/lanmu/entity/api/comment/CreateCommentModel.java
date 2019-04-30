package slasha.lanmu.entity.api.comment;

import com.google.gson.annotations.Expose;

public class CreateCommentModel {
    @Expose
    private long postId;
    @Expose
    private long fromId;
    @Expose
    private String content;

    private String fromName;

    public CreateCommentModel() {
    }

    public CreateCommentModel(long postId, long fromId) {
        this.postId = postId;
        this.fromId = fromId;
    }


    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
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
}
