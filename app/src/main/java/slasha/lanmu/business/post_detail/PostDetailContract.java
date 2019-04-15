package slasha.lanmu.business.post_detail;

import java.util.List;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import slasha.lanmu.BaseModel;
import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.bean.BookPost;
import slasha.lanmu.bean.Comment;
import slasha.lanmu.widget.reply.Publisher;

public interface PostDetailContract {

    interface PostDetailView extends BaseView<PostDetailPresenter> {
        @MainThread
        void showDetail(BookPost bookPost);

        @MainThread
        void showComments(List<Comment> comments);
    }

    interface PostDetailPresenter extends BasePresenter {
        void performPullComments(long postId);

        void performPullPostDetail();

        void performPublishComment(Publisher.CommentData commentData, String content);

        void performPublishCommentReply(Publisher.CommentReplyData commentReplyData, String content);
    }

    interface PostDetailModel extends BaseModel {
        @WorkerThread
        void offerComments(long postId, Callback<List<Comment>> listCallback);
    }
}
