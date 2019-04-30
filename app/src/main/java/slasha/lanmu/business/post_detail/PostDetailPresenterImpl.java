package slasha.lanmu.business.post_detail;

import slasha.lanmu.LoadingProvider;
import slasha.lanmu.entity.api.base.RspModelWrapper;
import slasha.lanmu.entity.api.comment.CreateCommentModel;
import slasha.lanmu.entity.card.CommentCard;
import slasha.lanmu.net.Network;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.PresenterHelper;
import slasha.lanmu.widget.reply.Publisher;

public class PostDetailPresenterImpl implements PostDetailContract.Presenter {

    private static final String TAG = "lanmu.comment";
    private PostDetailContract.View mView;
    private PostDetailContract.PostDetailModel mModel;

    PostDetailPresenterImpl(PostDetailContract.View postDetailView,
                            PostDetailContract.PostDetailModel postDetailModel) {
        mView = postDetailView;
        mModel = postDetailModel;
    }

    @Override
    public void performPullComments(long postId) {
        mView.showLoadingIndicator();
        mModel.offerComments(postId, comments ->
                AppUtils.runOnUiThread(() -> {
                    mView.showComments(comments);
                    mView.hideLoadingIndicator();
                })
        );
    }

    @Override
    public void performPullPostDetail() {
        // load nothing because data are from last activity
    }

    @Override
    public void performPublishComment(CreateCommentModel model, LoadingProvider loadingProvider) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote()::createComment,
                model,
                mView::showCreateCommentSuccess,
                mView::showActionFail,
                loadingProvider
        );
    }

    @Override
    public void performPublishCommentReply(Publisher.CommentReplyData commentReplyData, String content) {

    }
}
