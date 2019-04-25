package slasha.lanmu.entity.card;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;

import slasha.lanmu.entity.db.User;


public class BookPostCard {

    @Expose
    private BookCard book;
    @Expose
    private User creator;
    @Expose
    private LocalDateTime createDate;
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

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
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
