package slasha.lanmu.business.post_detail.reply;

import androidx.annotation.MainThread;
import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.LoadingProvider;
import slasha.lanmu.entity.api.base.PageModel;
import slasha.lanmu.entity.api.comment.CreateReplyModel;
import slasha.lanmu.entity.card.CommentReplyCard;

public interface ReplyContract {

    interface View extends BaseView<Presenter> {
        @MainThread
        void showPullReliesSuccess(PageModel<CommentReplyCard> card);

        @MainThread
        void showCreateReplySuccess(CommentReplyCard card);
    }

    interface Presenter extends BasePresenter {

        void performPullReplies(long commentId, int page);

        void performPublishCommentReply(CreateReplyModel model);

    }

}
