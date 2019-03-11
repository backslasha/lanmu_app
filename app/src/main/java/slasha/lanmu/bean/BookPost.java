package slasha.lanmu.bean;

import java.util.List;

public class BookPost {
    private long id;
    private Book book;
    private List<Comment> comments;
    private CreateInfo createInfo;

    public BookPost(long id, Book book, List<Comment> comments, CreateInfo createInfo) {
        this.id = id;
        this.book = book;
        this.comments = comments;
        this.createInfo = createInfo;
    }

    @Override
    public String toString() {
        return "BookPost{" +
                "id=" + id +
                ", book=" + book +
                ", comments=" + comments +
                ", createInfo=" + createInfo +
                '}';
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public CreateInfo getCreateInfo() {
        return createInfo;
    }

    public void setCreateInfo(CreateInfo createInfo) {
        this.createInfo = createInfo;
    }
}
