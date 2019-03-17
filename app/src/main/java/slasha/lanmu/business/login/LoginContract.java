package slasha.lanmu.business.login;

import slasha.lanmu.BaseModel;
import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.bean.LoginResult;
import slasha.lanmu.bean.User;

public interface LoginContract {

    interface LoginView extends BaseView<LoginPresenter> {
        void showLoginResult(boolean success);
    }

    interface LoginPresenter extends BasePresenter {
        void performLogin(String email, String password);
    }

    interface LoginModel extends BaseModel {
        LoginResult offerLoginResult(String email, String password);
    }
}
