package slasha.lanmu.business.profile;

import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.local.User;

public interface ProfileContract {

    interface ProfileView extends BaseView<ProfilePresenter> {
        void showProfile(User user);
    }

    interface ProfilePresenter extends BasePresenter {

    }

}
