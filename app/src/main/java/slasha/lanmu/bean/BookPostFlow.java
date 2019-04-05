package slasha.lanmu.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class BookPostFlow implements Serializable {

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("book_posts")
    private List<BookPost> bookPosts;

    public BookPostFlow(String name, List<BookPost> bookPosts) {
        this.name = name;
        this.bookPosts = bookPosts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BookPost> getBookPosts() {
        return bookPosts;
    }

    public void setBookPosts(List<BookPost> bookPosts) {
        this.bookPosts = bookPosts;
    }
}
