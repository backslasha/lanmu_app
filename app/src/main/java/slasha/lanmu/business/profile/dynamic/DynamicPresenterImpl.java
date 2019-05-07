package slasha.lanmu.business.profile.dynamic;

import slasha.lanmu.entity.card.DynamicCard;
import slasha.lanmu.net.Network;
import slasha.lanmu.utils.PresenterHelper;

class DynamicPresenterImpl implements DynamicContract.Presenter {

    private static final String TAG = "lanmu.dynamic";

    private DynamicContract.View mView;

    DynamicPresenterImpl(DynamicContract.View view) {
        mView = view;
    }


    @Override
    public void performPullThumbsUps(long userId) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote()::pullThumbsUpDynamics,
                userId,
                mView::showPullThumbsUpsSuccess,
                mView::showActionFail,
                mView
        );
    }

    @Override
    public void performPullComments(long userId) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote()::pullCommentsDynamics,
                userId,
                mView::showPullCommentsSuccess,
                mView::showActionFail,
                mView
        );
    }

    @Override
    public void performPullReplies(long userId) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote()::pullRepliesDynamics,
                userId,
                mView::showPullRepliesSuccess,
                mView::showActionFail,
                mView
        );
    }

    @Override
    public void performPullPosts(long userId) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote()::pullPostsDynamics,
                userId,
                mView::showPullPostsSuccess,
                mView::showActionFail,
                mView
        );
    }

    @Override
    public void performPullDynamicByType(long userId, int type) {
        switch (type) {
            case DynamicCard.TYPE_CREATE_POST:
                performPullPosts(userId);
                break;
            case DynamicCard.TYPE_COMMENT:
                performPullComments(userId);
                break;
            case DynamicCard.TYPE_COMMENT_REPLY:
                performPullReplies(userId);
                break;
            case DynamicCard.TYPE_THUMB_UP:
                performPullThumbsUps(userId);
                break;
        }
    }
}
