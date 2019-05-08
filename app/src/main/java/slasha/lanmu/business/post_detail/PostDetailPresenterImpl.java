package slasha.lanmu.business.post_detail;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import slasha.lanmu.LoadingProvider;
import slasha.lanmu.entity.api.base.RspModelWrapper;
import slasha.lanmu.entity.api.comment.CreateCommentModel;
import slasha.lanmu.entity.api.comment.CreateReplyModel;
import slasha.lanmu.entity.card.CommentCard;
import slasha.lanmu.net.Network;
import slasha.lanmu.utils.PresenterHelper;
import slasha.lanmu.utils.common.LogUtil;

public class PostDetailPresenterImpl implements PostDetailContract.Presenter {

    private static final String TAG = "lanmu.comment";
    private PostDetailContract.View mView;

    PostDetailPresenterImpl(PostDetailContract.View postDetailView) {
        mView = postDetailView;
    }


    @Override
    public void performPullPostDetail(long postId) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote()::searchPostById,
                postId,
                mView::showDetail,
                mView::showActionFail,
                mView
        );
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
    public void performPublishCommentReply(CreateReplyModel model, LoadingProvider loadingProvider) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote()::createCommentReply,
                model,
                mView::showCreateReplySuccess,
                mView::showActionFail,
                loadingProvider
        );
    }

    @Override
    public void performThumbsUp(long fromId, long commentId) {
        Call<RspModelWrapper> rspModelWrapperCall
                = Network.remote().doThumbsUp(commentId, fromId);
        rspModelWrapperCall.enqueue(new Callback<RspModelWrapper>() {
            @Override
            public void onResponse(Call<RspModelWrapper> call, Response<RspModelWrapper> response) {
                LogUtil.i(TAG, "thumbs up succeed: " + response.body());
            }

            @Override
            public void onFailure(Call<RspModelWrapper> call, Throwable t) {
                LogUtil.e(TAG, "thumbs up failed: " + t.getCause());
            }
        });
    }

    @Override
    public void performPullComments(long postId, int order, int page, LoadingProvider loadingProvider) {
        if (page > 1) {
            loadingProvider = new LoadingProvider() {
                @Override
                public void showLoadingIndicator() {

                }

                @Override
                public void hideLoadingIndicator() {

                }
            };
        }
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote().pullComments(postId, order, page),
                mView::showComments,
                mView::showActionFail,
                loadingProvider
        );
    }

}
