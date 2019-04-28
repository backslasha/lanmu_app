package slasha.lanmu.business.profile.edit;

import androidx.annotation.UiThread;
import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.card.UserCard;

public interface EditProfileContract {

    interface View extends BaseView<Presenter> {

        @UiThread
        void showProfileUpdateSuccess(UserCard user);
    }

    interface Presenter extends BasePresenter {

        void performSubmitUserInfo(UserCard userCard);
    }

}
