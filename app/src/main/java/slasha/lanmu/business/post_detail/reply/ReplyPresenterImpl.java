package slasha.lanmu.business.post_detail.reply;

import slasha.lanmu.LoadingProvider;
import slasha.lanmu.entity.api.comment.CreateReplyModel;
import slasha.lanmu.net.Network;
import slasha.lanmu.utils.PresenterHelper;

class ReplyPresenterImpl implements ReplyContract.Presenter {

    private static final String TAG = "lanmu.reply";
    private ReplyContract.View mView;

    ReplyPresenterImpl(ReplyContract.View view) {
        mView = view;
    }

    @Override
    public void performPullReplies(long commentId, int page) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote().pullReplies(commentId, page),
                mView::showPullReliesSuccess,
                mView::showActionFail,
                new LoadingProvider() {
                    @Override
                    public void showLoadingIndicator() {

                    }

                    @Override
                    public void hideLoadingIndicator() {

                    }
                }
        );
    }


    @Override
    public void performPublishCommentReply(CreateReplyModel model) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote()::createCommentReply,
                model,
                mView::showCreateReplySuccess,
                mView::showActionFail,
                mView
        );
    }

}
