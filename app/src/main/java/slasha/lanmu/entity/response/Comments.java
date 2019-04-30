package slasha.lanmu.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import slasha.lanmu.entity.card.CommentCard;
import slasha.lanmu.entity.local.Comment;

public class Comments implements Serializable {
    @Expose
    @SerializedName("comments")
    List<CommentCard> comments;
    @Expose
    @SerializedName("status")
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<CommentCard> getComments() {
        return comments;
    }

    public void setComments(List<CommentCard> comments) {
        this.comments = comments;
    }
}
