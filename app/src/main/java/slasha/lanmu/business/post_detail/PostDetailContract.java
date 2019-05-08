package slasha.lanmu.business.post_detail;

import java.util.List;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import slasha.lanmu.BaseModel;
import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.LoadingProvider;
import slasha.lanmu.entity.api.base.PageModel;
import slasha.lanmu.entity.api.comment.CreateCommentModel;
import slasha.lanmu.entity.api.comment.CreateReplyModel;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.entity.card.CommentCard;
import slasha.lanmu.entity.card.CommentReplyCard;
import slasha.lanmu.entity.local.BookPost;
import slasha.lanmu.entity.local.Comment;
import slasha.lanmu.entity.local.CommentReply;
import slasha.lanmu.widget.reply.Publisher;

public interface PostDetailContract {

    interface View extends BaseView<Presenter> {
        @MainThread
        void showDetail(List<BookPostCard> bookPosts);

        @MainThread
        void showComments(PageModel<CommentCard> commentPage);

        @MainThread
        void showCreateCommentSuccess(CommentCard card);

        @MainThread
        void showCreateReplySuccess(CommentReplyCard card);
    }

    interface Presenter extends BasePresenter {

        void performPullPostDetail(long postId);

        void performPublishComment(CreateCommentModel model, LoadingProvider loadingProvider);

        void performPublishCommentReply(CreateReplyModel model, LoadingProvider loadingProvider);

        void performThumbsUp(long fromId, long commentId);

        void performPullComments(long postId, int order, int page, LoadingProvider loadingProvider);
    }

}
