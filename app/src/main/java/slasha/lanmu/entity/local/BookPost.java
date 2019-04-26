
package slasha.lanmu.entity.local;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.sql.Time;

public class BookPost implements Serializable {

    @Expose
    private long id;

    @Expose
    private Book book;

    @Expose
    private User creator;

    @Expose
    private java.sql.Time createTime;

    @Expose
    private java.sql.Time recentReplyTime;

    @Expose
    private String content;

    @Expose
    private String images;

    @Expose
    private int commentCount;

    public BookPost(long id, Book book, User creator,
                    Time createTime, Time recentReplyTime, String content, int commentCount) {
        this.id = id;
        this.book = book;
        this.creator = creator;
        this.createTime = createTime;
        this.recentReplyTime = recentReplyTime;
        this.content = content;
        this.commentCount = commentCount;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Time getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Time createTime) {
        this.createTime = createTime;
    }

    public Time getRecentReplyTime() {
        return recentReplyTime;
    }

    public void setRecentReplyTime(Time recentReplyTime) {
        this.recentReplyTime = recentReplyTime;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
