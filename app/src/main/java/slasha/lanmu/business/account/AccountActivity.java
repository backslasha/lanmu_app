package slasha.lanmu.business.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import slasha.lanmu.BaseActivity;
import slasha.lanmu.R;
import slasha.lanmu.business.account.fragment.AccountTrigger;
import slasha.lanmu.business.account.fragment.LoginFragment;
import slasha.lanmu.business.account.fragment.RegisterFragment;
import slasha.lanmu.business.main.MainActivity;
import slasha.lanmu.persistence.AccountInfo;
import slasha.lanmu.persistence.UserInfo;

public class AccountActivity extends BaseActivity implements AccountTrigger {

    private Fragment mCurFragment;
    private Fragment mLoginFragment;
    private Fragment mRegisterFragment;

    @BindView(R.id.im_bg)
    ImageView mBg;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AccountActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化Fragment
        mCurFragment = mLoginFragment = new LoginFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container, mCurFragment)
                .commit();

    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        boolean loggedInLocally = AccountInfo.loggedIn();
        if (!loggedInLocally) {
            AccountInfo.load(this);
            UserInfo.load();
            loggedInLocally = AccountInfo.loggedIn();
        }
        if (!loggedInLocally) {
            return true;
        }else { // already loggedIn, jump to main page
            startActivity(MainActivity.newIntent(this));
            return false;
        }
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_account;
    }

    @Override
    public void triggerView() {
        Fragment fragment;
        if (mCurFragment == mLoginFragment) {
            if (mRegisterFragment == null) {
                //默认情况下为null，
                //第一次之后就不为null了
                mRegisterFragment = new RegisterFragment();
            }
            fragment = mRegisterFragment;
        } else {
            // 因为默认请求下mLoginFragment已经赋值，无须判断null
            fragment = mLoginFragment;
        }

        // 重新赋值当前正在显示的Fragment
        mCurFragment = fragment;
        // 切换显示ø
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.lay_container, fragment)
                .commit();
    }
}

