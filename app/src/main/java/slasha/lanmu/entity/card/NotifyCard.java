package slasha.lanmu.entity.card;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class NotifyCard {

    public static final int TYPE_NEW_COMMENT = 1;
    public static final int TYPE_NEW_REPLY = 2;
    public static final int TYPE_NEW_THUMBS_UP = 3;

    @Expose
    private int type;

    @Expose
    private long postId;

    @Expose
    private long id;

    @Expose
    private UserCard from;
    @Expose
    private Date time;
    @Expose
    private String content1;
    @Expose
    private String content2;
    @Expose
    private String cover;
    @Expose
    private BookCard book;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserCard getFrom() {
        return from;
    }

    public void setFrom(UserCard from) {
        this.from = from;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"type\":")
                .append(type);
        sb.append(",\"postId\":")
                .append(postId);
        sb.append(",\"id\":")
                .append(id);
        sb.append(",\"from\":")
                .append(from);
        sb.append(",\"time\":\"")
                .append(time).append('\"');
        sb.append(",\"content1\":\"")
                .append(content1).append('\"');
        sb.append(",\"content2\":\"")
                .append(content2).append('\"');
        sb.append(",\"cover\":\"")
                .append(cover).append('\"');
        sb.append(",\"book\":")
                .append(book);
        sb.append('}');
        return sb.toString();
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public BookCard getBook() {
        return book;
    }

    public void setBook(BookCard book) {
        this.book = book;
    }


}
