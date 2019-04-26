package slasha.lanmu.entity.card;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.util.Date;

import slasha.lanmu.entity.db.User;


public class BookPostCard {

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
