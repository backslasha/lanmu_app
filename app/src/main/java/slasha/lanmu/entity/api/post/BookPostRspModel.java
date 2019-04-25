package slasha.lanmu.entity.api.post;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

import slasha.lanmu.entity.card.BookPostCard;


/**
 * 请求书帖时返回的信息
 */
public class BookPostRspModel implements Serializable {

    @Expose
    private List<BookPostCard> posts;

    @Expose
    private int total;

    public BookPostRspModel(List<BookPostCard> posts, int total) {
        this.posts = posts;
        this.total = total;
    }

    public List<BookPostCard> getPosts() {
        return posts;
    }

    public void setPosts(List<BookPostCard> posts) {
        this.posts = posts;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


}
