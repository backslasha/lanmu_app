package slasha.lanmu.entity.card;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Date;

import slasha.lanmu.entity.local.User;


public class BookPostCard implements Serializable {

    @Expose
    private BookCard book;
    @Expose
    private User creator;
    @Expose
    private Date createDate;
    @Expose
    private String content;
    @Expose
    private String images;
    @Expose
    private int commentCount;

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Expose
    private long id;


    public BookCard getBook() {
        return book;
    }

    public void setBook(BookCard book) {
        this.book = book;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }


}
