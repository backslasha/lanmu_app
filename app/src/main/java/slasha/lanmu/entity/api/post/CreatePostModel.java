package slasha.lanmu.entity.api.post;

import com.google.gson.annotations.Expose;

import slasha.lanmu.entity.local.Book;


/**
 * 创建书帖时需要的信息
 */
public class CreatePostModel {

    @Expose
    private Book  book;
    @Expose
    private long createId;
    @Expose
    private String content;
    @Expose
    private String images;

    public CreatePostModel(Book book, long createId, String content, String images) {
        this.book = book;
        this.createId = createId;
        this.content = content;
        this.images = images;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public long getCreateId() {
        return createId;
    }

    public void setCreateId(long createId) {
        this.createId = createId;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"book\":")
                .append(book);
        sb.append(",\"createId\":")
                .append(createId);
        sb.append(",\"content\":\"")
                .append(content).append('\"');
        sb.append(",\"images\":\"")
                .append(images).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
