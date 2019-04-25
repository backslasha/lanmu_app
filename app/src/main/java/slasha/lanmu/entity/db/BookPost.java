package slasha.lanmu.entity.db;


import java.time.LocalDateTime;

public class BookPost {

    public BookPost(Book book, long creatorId, String content, String images) {
        this.book = book;
        this.creatorId = creatorId;
        this.content = content;
        this.images = images;
    }

    private long id;

    private Book book;

    private long bookId;

    private User creator;

    private long creatorId;

    private LocalDateTime createDate = LocalDateTime.now();

    private String content;

    private String images;

    public BookPost() {
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }


    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
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
}
