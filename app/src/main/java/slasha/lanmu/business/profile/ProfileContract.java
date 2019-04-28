package slasha.lanmu.business.profile;

import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.card.UserCard;

public interface ProfileContract {

    interface ProfileView extends BaseView<ProfilePresenter> {
        void showProfile(UserCard user);
    }

    interface ProfilePresenter extends BasePresenter {

    }

}
