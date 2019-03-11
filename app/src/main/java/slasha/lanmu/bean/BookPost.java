
package slasha.lanmu.bean;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookPost implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("book")
    @Expose
    private Book book;
    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;
    @SerializedName("createInfo")
    @Expose
    private CreateInfo createInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
