package slasha.lanmu.business.friend;

import slasha.lanmu.net.Network;
import slasha.lanmu.utils.PresenterHelper;

class FriendPresenterImpl implements FriendContract.Presenter {

    private FriendContract.View mView;
    private static final String TAG = "lanmu.friend";

    FriendPresenterImpl(FriendContract.View view) {
        mView = view;
    }

    @Override
    public void performPullFriends(long userId) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote()::pullFriends,
                userId,
                mView::showPullFriendsSuccess,
                mView::showActionFail,
                mView
        );
    }
}
