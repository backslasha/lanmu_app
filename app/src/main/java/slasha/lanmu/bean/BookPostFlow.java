package slasha.lanmu.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BookPostFlow implements Serializable {

    @Expose
    @SerializedName("total_count")
    private int totalCount;
    @Expose
    @SerializedName("hot_count")
    private int hotCount;
    @Expose
    @SerializedName("suggest_count")
    private int suggestCount;
    @Expose
    @SerializedName("history_count")
    private int historyCount;
    @Expose
    @SerializedName("book_posts")
    private List<BookPost> bookPosts;

    public BookPostFlow(int totalCount, int hotCount, int suggestCount, int historyCount,
                        List<BookPost> bookPosts) {
        this.totalCount = totalCount;
        this.hotCount = hotCount;
        this.suggestCount = suggestCount;
        this.historyCount = historyCount;
        this.bookPosts = bookPosts;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getHotCount() {
        return hotCount;
    }

    public void setHotCount(int hotCount) {
        this.hotCount = hotCount;
    }

    public int getSuggestCount() {
        return suggestCount;
    }

    public void setSuggestCount(int suggestCount) {
        this.suggestCount = suggestCount;
    }

    public int getHistoryCount() {
        return historyCount;
    }

    public void setHistoryCount(int historyCount) {
        this.historyCount = historyCount;
    }

    public List<BookPost> getBookPosts() {
        return bookPosts;
    }

    public void setBookPosts(List<BookPost> bookPosts) {
        this.bookPosts = bookPosts;
    }

    @Override
    public String toString() {
        return "BookPostFlow{" +
                "totalCount=" + totalCount +
                ", hotCount=" + hotCount +
                ", suggestCount=" + suggestCount +
                ", historyCount=" + historyCount +
                ", bookPosts=" + bookPosts +
                '}';
    }
}
