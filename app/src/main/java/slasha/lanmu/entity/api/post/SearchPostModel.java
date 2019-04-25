package slasha.lanmu.entity.api.post;

import com.google.gson.annotations.Expose;


/**
 * 搜索书帖时需要的信息
 */
public class SearchPostModel {

    @Expose
    private String keyword;
    @Expose
    private long postId;

    public SearchPostModel(long postId) {
        this.postId = postId;
    }

    public SearchPostModel(String keyword) {
        this.keyword = keyword;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
