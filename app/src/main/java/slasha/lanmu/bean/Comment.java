package slasha.lanmu.bean;

public class Comment {
    private User from;
    private User to;
    private String content;
    private long postId;

    public Comment(User from, User to, String content, long postId) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.postId = postId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "from=" + from +
                ", to=" + to +
                ", content='" + content + '\'' +
                ", postId=" + postId +
                '}';
    }
}
