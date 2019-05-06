package slasha.lanmu.business.friend.apply;

import java.util.List;

import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.LoadingProvider;
import slasha.lanmu.entity.card.ApplyCard;
import slasha.lanmu.entity.card.UserCard;

public interface ApplyContract {
    interface View extends BaseView<Presenter> {
        void showPullAppliesSuccess(List<ApplyCard> cards);

        void showAddFriendSuccess(UserCard userCard);

        void showRejectApplySuccess(ApplyCard applyCard);
    }

    interface Presenter extends BasePresenter {
        void performPullApplies(long userId);

        void performAddFriend(long fromId, long toId, LoadingProvider loadingProvider);

        void performRejectApply(long applyId, LoadingProvider loadingProvider);
    }
}
