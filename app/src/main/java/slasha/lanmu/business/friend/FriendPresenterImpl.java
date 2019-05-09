package slasha.lanmu.business.friend;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import slasha.lanmu.entity.api.base.RspModelWrapper;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.entity.local.User;
import slasha.lanmu.net.Network;
import slasha.lanmu.persistence.db.LanmuDB;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.PresenterHelper;
import slasha.lanmu.utils.common.LogUtil;
import slasha.lanmu.utils.common.ThreadUtils;

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

    @Override
    public void performSyncFriends2Db(List<UserCard> cards) {
        if (cards != null) {
            ThreadUtils.execute(() ->
                    LanmuDB.saveFriends(cards.toArray(new UserCard[0])));
        }
    }

    @Override
    public void performPullFriendLocally(long userId) {
        mView.showLoadingIndicator();
        ThreadUtils.execute(() -> {
            List<UserCard> userCards = LanmuDB.queryFriends();
            AppUtils.runOnUiThread(() -> {
                mView.hideLoadingIndicator();
                mView.showPullFriendsLocallySuccess(userCards);
            });
        });
    }
}
