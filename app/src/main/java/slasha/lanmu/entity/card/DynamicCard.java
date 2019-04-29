package slasha.lanmu.entity.card;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import androidx.annotation.Nullable;

public class DynamicCard implements Serializable {

    public static final int TYPE_CREATE_POST = 1; // 创建帖子
    public static final int TYPE_COMMENT = 2; // 回复帖子
    public static final int TYPE_COMMENT_REPLY = 3; // 回复评论（回复评论+回复评论下他人的评论）
    public static final int TYPE_THUMB_UP = 4; // 点赞评论



    @Expose
    private int type;

    @Expose
    private BookPostCard bookPostCard;

    @Expose
    @Nullable
    private CommentCard commentCard;

    @Expose
    @Nullable
    private CommentReplyCard commentReplyCard;

    public BookPostCard getBookPostCard() {
        return bookPostCard;
    }

    public void setBookPostCard(BookPostCard bookPostCard) {
        this.bookPostCard = bookPostCard;
    }

    @Nullable
    public CommentCard getCommentCard() {
        return commentCard;
    }

    public void setCommentCard(@Nullable CommentCard commentCard) {
        this.commentCard = commentCard;
    }

    @Nullable
    public CommentReplyCard getCommentReplyCard() {
        return commentReplyCard;
    }

    public void setCommentReplyCard(@Nullable CommentReplyCard commentReplyCard) {
        this.commentReplyCard = commentReplyCard;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"type\":")
                .append(type);
        sb.append(",\"bookPostCard\":")
                .append(bookPostCard);
        sb.append(",\"commentCard\":")
                .append(commentCard);
        sb.append(",\"commentReplyCard\":")
                .append(commentReplyCard);
        sb.append('}');
        return sb.toString();
    }
}
