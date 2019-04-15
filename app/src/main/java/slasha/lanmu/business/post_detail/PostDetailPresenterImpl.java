package slasha.lanmu.business.post_detail;

import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.widget.reply.Publisher;
import slasha.lanmu.widget.reply.ReplyPublisher;

public class PostDetailPresenterImpl implements PostDetailContract.PostDetailPresenter {

    private PostDetailContract.PostDetailView mView;
    private PostDetailContract.PostDetailModel mModel;
    private ReplyPublisher mReplyPublisher;

    PostDetailPresenterImpl(PostDetailContract.PostDetailView postDetailView,
                            PostDetailContract.PostDetailModel postDetailModel,
                            ReplyPublisher.ReplyStatusListener replyStatusListener
    ) {
        mView = postDetailView;
        mModel = postDetailModel;
        mReplyPublisher = new ReplyPublisher();
        mReplyPublisher.setStatusListener(replyStatusListener);
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
    public void performPublishComment(Publisher.CommentData commentData, String content) {
        mReplyPublisher.publishComment((Publisher.CommentData) commentData.clone(), content);
    }

    @Override
    public void performPublishCommentReply(Publisher.CommentReplyData commentReplyData, String content) {
        mReplyPublisher.publishCommentReply((Publisher.CommentReplyData) commentReplyData.clone(), content);
    }
}
