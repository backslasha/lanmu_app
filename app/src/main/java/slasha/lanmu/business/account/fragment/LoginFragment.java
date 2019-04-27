package slasha.lanmu.business.account.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import slasha.lanmu.BaseActivity;
import slasha.lanmu.BaseFragment;
import slasha.lanmu.R;
import slasha.lanmu.business.account.AccountContract;
import slasha.lanmu.business.account.AccountPresenterImpl;
import slasha.lanmu.business.main.MainActivity;
import slasha.lanmu.entity.api.account.LoginModel;
import slasha.lanmu.entity.local.User;
import slasha.lanmu.utils.LogUtil;
import slasha.lanmu.utils.ToastUtils;

/**
 * 登录的界面
 */
public class LoginFragment extends BaseFragment
        implements AccountContract.View {

    private static final String TAG = "lanmu.account";
    private AccountTrigger mAccountTrigger;
    private BaseActivity mBaseActivity;
    private AccountContract.Presenter mPresenter;

    @BindView(R.id.edit_phone)
    EditText mPhone;

    @BindView(R.id.edit_password)
    EditText mPassword;

//    @BindView(R.id.edit_url)
//    EditText mUrl;
//
//    @OnClick(R.id.btn_test)
//    void test() {
//        ThreadUtils.execute(() -> {
//            Request request = new Request.Builder().get().url(String.valueOf(mUrl.getText())).build();
//            Network.getClient()
//                    .newCall(request)
//                    .enqueue(new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                            AppUtils.runOnUiThread(() -> ToastUtils.showToast("fail"));
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//                            AppUtils.runOnUiThread(() -> ToastUtils.showToast("success"));
//                        }
//                    });
//        });
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAccountTrigger = (AccountTrigger) context;
        mBaseActivity = (BaseActivity) context;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @OnClick(R.id.txt_go_register)
    void switch2Register() {
        mAccountTrigger.triggerView();
    }

    @OnClick(R.id.btn_login)
    void login() {
        final String password = String.valueOf(mPassword.getText());
        final String phone = String.valueOf(mPhone.getText());
        if (checkInputCorrection(password, phone)) {
            myPresenter().performLogin(new LoginModel(phone, password));
        }
    }

    @Override
    public AccountContract.Presenter myPresenter() {
        if (mPresenter == null) {
            mPresenter = new AccountPresenterImpl(this, getContext());
        }
        return mPresenter;
    }

    @Override
    public void showLoadingIndicator() {
        if (mBaseActivity != null)
            mBaseActivity.showProgressDialog();
    }

    @Override
    public void hideLoadingIndicator() {
        if (mBaseActivity != null)
            mBaseActivity.hideProgressDialog();
    }

    @Override
    public void showActionFail(String info) {
        ToastUtils.showToast(mBaseActivity.getString(R.string.login_fail) + ":" + info);
    }

    @Override
    public void showActionSuccess(User user) {
        ToastUtils.showToast(R.string.login_success);
        jumpToMainPage();
    }

    private void jumpToMainPage() {
        if (mBaseActivity != null) {
            mBaseActivity.finish();
        }
        startActivity(
                MainActivity.newIntent(getActivity())
        );
    }

    private boolean checkInputCorrection(String password, String phone) {
        // Reset errors.
        mPhone.setError(null);
        mPassword.setError(null);

        boolean cancel = false;
        android.view.View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        // Check for a valid phone address.
        if (!isPhoneValid(phone)) {
            mPhone.setError(getString(R.string.error_invalid_phone));
            focusView = mPhone;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }

        return !cancel;
    }

    // 手机号不为空，并且满足格式
    private boolean isPhoneValid(String phone) {
        final String REGEX_MOBILE = "[1][34578][0-9]{9}$";
        return !TextUtils.isEmpty(phone)
                && Pattern.matches(REGEX_MOBILE, phone);
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.length() >= 6;
    }

}
