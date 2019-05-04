package slasha.lanmu.business.contract;

import java.util.List;

import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.card.UserCard;

public interface FriendContract {
    interface View extends BaseView<Presenter> {
        void showPullFriendsSuccess(List<UserCard> cards);
    }

    interface Presenter extends BasePresenter {
        void performPullFirends(long userId);
    }
}
