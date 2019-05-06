package slasha.lanmu.business.profile;

import java.util.List;

import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.card.DynamicCard;
import slasha.lanmu.entity.card.UserCard;

public interface ProfileContract {

    interface View extends BaseView<Presenter> {
        void showProfile(UserCard user);

        void showPullDynamicsSuccess(List<DynamicCard> dynamics);

        void showSendApplySuccess();
    }

    interface Presenter extends BasePresenter {

        void performPullDynamics(long userId);

        void performPullProfile(long userId);

        void performSendFriendApply(long fromId, long toId);
    }

}
