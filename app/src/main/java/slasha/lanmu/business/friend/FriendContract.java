package slasha.lanmu.business.friend;

import java.util.List;

import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.card.UserCard;

public interface FriendContract {

    interface View extends BaseView<Presenter> {
        void showPullFriendsLocallySuccess(List<UserCard> cards);

        void showPullFriendsSuccess(List<UserCard> cards);
    }

    interface Presenter extends BasePresenter {

        void performPullFriends(long userId);

        void performSyncFriends2Db(List<UserCard> cards);

        void performPullFriendLocally(long userId);
    }
}
