package slasha.lanmu.business.post_detail;

import slasha.lanmu.utils.AppUtils;

public class PostDetailPresenterImpl implements PostDetailContract.PostDetailPresenter {

    private PostDetailContract.PostDetailView mView;
    private PostDetailContract.PostDetailModel mModel;

    PostDetailPresenterImpl(PostDetailContract.PostDetailView postDetailView,
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
}
