package slasha.lanmu.widget.reply;

public interface Publisher {
    void publishComment(CommentData commentData, String content);

    void publishCommentReply(CommentReplyData commentReplyData, String content);

    class CommentData implements Cloneable {
        private long postId;
        private long fromId;

        public CommentData(long postId, long fromId) {
            this.postId = postId;
            this.fromId = fromId;
        }

        public long getPostId() {
            return postId;
        }

        public long getFromId() {
            return fromId;
        }

        public boolean prepared() {
            return postId != -1 && fromId != -1;
        }

        public void clean() {

        }

        @Override
        public String toString() {
            return "CommentData{" +
                    "postId=" + postId +
                    ", fromId=" + fromId +
                    '}';
        }

        @Override
        public Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class CommentReplyData implements Cloneable {
        long toId;
        private long postId;
        private long commentId;
        private long fromId;

        public CommentReplyData(long postId, long fromId) {
            this.postId = postId;
            this.fromId = fromId;
        }

        public long getPostId() {
            return postId;
        }

        public long getCommentId() {
            return commentId;
        }

        public long getFromId() {
            return fromId;
        }

        public long getToId() {
            return toId;
        }

        public void prepare(long commentId, long toId) {
            this.toId = toId;
            this.commentId = commentId;
        }

        public void clean() {
            this.toId = -1;
            this.commentId = -1;
        }

        public boolean prepared() {
            return postId != -1
                    && commentId != -1
                    && fromId != -1;
        }

        @Override
        public String toString() {
            return "CommentReplyData{" +
                    "toId=" + toId +
                    ", postId=" + postId +
                    ", commentId=" + commentId +
                    ", fromId=" + fromId +
                    '}';
        }

        @Override
        public Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

