
package slasha.lanmu.bean;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookPosts {

    @SerializedName("bookPosts")
    @Expose
    private List<BookPost> bookPosts = null;

    public List<BookPost> getBookPosts() {
        return bookPosts;
    }

    public void setBookPosts(List<BookPost> bookPosts) {
        this.bookPosts = bookPosts;
    }

}
