
package slasha.lanmu.bean;

import java.io.Serializable;
import java.sql.Time;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookPost implements Serializable {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("book")
    @Expose
    private Book book;

    @SerializedName("creator")
    @Expose
    private User creator;

    @SerializedName("create_time")
    @Expose
    private java.sql.Time createTime;

    @SerializedName("recent_reply_time")
    @Expose
    private java.sql.Time recentReplyTime;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("comment_count")
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

    public static void main(String[] args) {
        System.out.println(new Gson().toJson(
                new BookPost(
                        5005078,
                        new Book(),
                        new User("username", "pwd", 5005078, ""),
                        new Time(System.currentTimeMillis()),
                        new Time(System.currentTimeMillis()),
                        "introduction",
                        999
                )
        ));
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
