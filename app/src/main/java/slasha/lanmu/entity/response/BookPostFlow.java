package slasha.lanmu.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import slasha.lanmu.entity.card.BookPostCard;


public class BookPostFlow implements Serializable {

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("book_posts")
    private List<BookPostCard> bookPosts;

    public BookPostFlow(String name, List<BookPostCard> bookPosts) {
        this.name = name;
        this.bookPosts = bookPosts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BookPostCard> getBookPosts() {
        return bookPosts;
    }

    public void setBookPosts(List<BookPostCard> bookPosts) {
        this.bookPosts = bookPosts;
    }
}
