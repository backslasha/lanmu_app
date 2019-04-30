package slasha.lanmu.business.post_detail;

import java.util.List;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import slasha.lanmu.BaseModel;
import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.LoadingProvider;
import slasha.lanmu.entity.api.comment.CreateCommentModel;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.entity.card.CommentCard;
import slasha.lanmu.entity.local.Comment;
import slasha.lanmu.widget.reply.Publisher;

public interface PostDetailContract {

    interface View extends BaseView<Presenter> {
        @MainThread
        void showDetail(BookPostCard bookPost);

        @MainThread
        void showComments(List<CommentCard> comments);

        @MainThread
        void showCreateCommentSuccess(CommentCard card);
    }

    interface Presenter extends BasePresenter {
        void performPullComments(long postId);

        void performPullPostDetail();

        void performPublishComment(CreateCommentModel model, LoadingProvider loadingProvider);

        void performPublishCommentReply(Publisher.CommentReplyData commentReplyData, String content);
    }

    interface PostDetailModel extends BaseModel {
        @WorkerThread
        void offerComments(long postId, Callback<List<CommentCard>> listCallback);
    }
}
