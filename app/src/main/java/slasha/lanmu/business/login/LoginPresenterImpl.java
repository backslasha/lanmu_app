package slasha.lanmu.business.login;

import android.support.annotation.WorkerThread;
import android.util.Log;

import slasha.lanmu.GlobalBuffer;
import slasha.lanmu.bean.LoginResult;
import slasha.lanmu.bean.User;
import slasha.lanmu.debug.ArtificialProductFactory;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.ThreadUtils;

public class LoginPresenterImpl implements LoginContract.LoginPresenter {

    private static final String TAG = "login";
    private LoginContract.LoginView mLoginView;
    private LoginContract.LoginModel mLoginModel;

    LoginPresenterImpl(LoginContract.LoginView loginView, LoginContract.LoginModel loginModel) {
        mLoginView = loginView;
        mLoginModel = loginModel;
    }


    @Override
    public void performLogin(String email, String password) {
        ThreadUtils.execute(() -> {

            final LoginResult loginResult = login(email, password);
            final boolean success = loginResult != null
                    && loginResult.getUser() != null
                    && loginResult.getStatus() == 200;

            if (success) {
                saveLoggedInUserInfo(loginResult.getUser());
                Log.i(TAG, loginResult.toString());
            } else {
                Log.e(TAG, "login fail -> " +
                        ((loginResult == null) ? "null" : loginResult.toString())
                );
            }
            AppUtils.runOnUiThread(
                    () -> mLoginView.showLoginResult(success)
            );
        });
    }


    private void saveLoggedInUserInfo(User user) {
        GlobalBuffer.AccountInfo.rememberLoginUser(user);
        // TODO: 2019/3/17 save to sp
    }

    @WorkerThread
    private LoginResult login(String email, String password) {
        try {
            // Simulate network access.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new IllegalArgumentException();
        }

        LoginResult loginResult;
        if (GlobalBuffer.Debug.sUserFakeLoginResult) {
            loginResult = ArtificialProductFactory.loginResult();
        } else {
            // TODO: 2019/3/17 pull login result from server
            loginResult = mLoginModel.offerLoginResult(email, password);
        }

        return loginResult;
    }
}
