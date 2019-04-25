package slasha.lanmu.entity.api.post;

import com.google.gson.annotations.Expose;

import slasha.lanmu.entity.card.BookCard;


/**
 * 创建书帖时需要的信息
 */
public class CreatePostModel {

    @Expose
    private BookCard book;
    @Expose
    private long createId;
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

    public CreatePostModel(BookCard book, long createId, String content, String images) {
        this.book = book;
        this.createId = createId;
        this.content = content;
        this.images = images;
    }

}
