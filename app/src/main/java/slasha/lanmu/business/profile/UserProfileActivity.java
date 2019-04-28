package slasha.lanmu.business.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.common.LogUtil;

public class UserProfileActivity extends SameStyleActivity implements ProfileContract.ProfileView {


    private static final String EXTRA_USER = "extra_user";
    private static final String TAG = "lanmu.profile";
    private UserCard mUserCard;

    @BindView(R.id.iv_avatar)
    ImageView mAvatar;

    @BindView(R.id.tv_username)
    TextView mUsername;

    @BindView(R.id.tv_user_description)
    TextView mDescription;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    public static Intent newIntent(Context context, UserCard user) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(EXTRA_USER, user);
        return intent;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mUserCard = (UserCard) bundle.getSerializable(EXTRA_USER);
        if (mUserCard == null) {
            LogUtil.e(TAG, "[UserProfileActivity] empty user card!");
            return false;
        }
        setTitle(mUserCard.getName());
        return true;
    }

    @Override
    protected void initData() {
        Picasso.with(this)
                .load(mUserCard.getAvatarUrl())
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.ic_default_avatar)
                .into(mAvatar);
        mUsername.setText(mUserCard.getName());
        String introduction = mUserCard.getIntroduction();
        if (TextUtils.isEmpty(introduction)) {
            mDescription.setText(R.string.default_user_description);
        } else {
            mDescription.setText(introduction);
        }
    }

    private boolean myself() {
        return mUserCard.getId() == UserInfo.id();
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_user_profile;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_profile, menu);
        MenuItem item;
        if (myself()) {
            item = menu.findItem(R.id.action_start_chat);
        } else {
            item = menu.findItem(R.id.action_edit_mode);
        }
        item.setVisible(false);
        item.setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_start_chat:
                AppUtils.jumpToChatPage(this, mUserCard);
                break;
            case R.id.action_edit_mode:
                AppUtils.jumpToEditProfilePage(this, mUserCard);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProfile(UserCard user) {
        // TODO: 2019/3/12 show profile
        setTitle(user.getName());
    }

    @Override
    public ProfileContract.ProfilePresenter myPresenter() {
        return null;
    }

    @Override
    public void showLoadingIndicator() {
        showProgressDialog();
    }

    @Override
    public void hideLoadingIndicator() {
        hideProgressDialog();
    }

    @Override
    public void showActionFail(String message) {

    }
}
