package slasha.lanmu.business.account;

import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.api.account.LoginModel;
import slasha.lanmu.entity.api.account.RegisterModel;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.entity.local.User;

public interface AccountContract {

    interface View extends BaseView<Presenter> {
        void showActionSuccess(UserCard user);
    }

    interface Presenter extends BasePresenter {
        void performLogin(LoginModel model);

        void performRegister(RegisterModel model);
    }

}
