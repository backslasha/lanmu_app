package slasha.lanmu.business.profile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.entity.card.DynamicCard;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.entity.db.User;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.DialogUtils;
import slasha.lanmu.utils.common.LogUtil;
import slasha.lanmu.utils.common.ToastUtils;

public class UserProfileActivity extends SameStyleActivity
        implements ProfileContract.View, UserInfo.UserInfoChangeListener {

    private static final String EXTRA_USER_ID = "extra_user_id";
    private static final String TAG = "lanmu.profile";
    private long mUserId;

    @BindView(R.id.iv_avatar)
    ImageView mAvatar;

    @BindView(R.id.tv_username)
    TextView mUsername;

    @BindView(R.id.tv_user_description)
    TextView mDescription;

    @BindView(R.id.tv_phone)
    TextView mPhone;

    @BindView(R.id.iv_sex_tag)
    ImageView mGender;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    private ProfileContract.Presenter mUserPresenterImpl;
    private UserCard mUserCard;

    public static Intent newIntent(Context context, long userId) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        return intent;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mUserId = bundle.getLong(EXTRA_USER_ID, -1);
        if (-1 == mUserId) {
            LogUtil.e(TAG, "[UserProfileActivity] -1 user id!");
            return false;
        }
        return true;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        AppBarLayout appbarLayout = findViewById(R.id.app_bar_layout);
        NestedScrollView scrollView = findViewById(R.id.nested_scroll_view);
        appbarLayout.addOnOffsetChangedListener((appBarLayout, offset) -> {
            if (scrollView.getTop() < mToolbar.getHeight()) {
                scrollView.setElevation(0);
                appbarLayout.setElevation(10);
            } else {
                scrollView.setElevation(10);
                appbarLayout.setElevation(0);
            }
        });

    }

    @Override
    protected void initData() {
        myPresenter().performPullProfile(mUserId);
    }

    private void initOrUpdateProfileUI(UserCard card) {
        CommonUtils.setAvatar(mAvatar, card.getAvatarUrl());
        mUsername.setText(card.getName());
        mPhone.setText(card.getPhone());

        boolean aBoy = "1".equals(card.getGender());
        Drawable drawable = getResources()
                .getDrawable(aBoy ? R.drawable.ic_sex_woman : R.drawable.ic_sex_man);
        mGender.setImageDrawable(drawable);
        mGender.getBackground().setLevel(aBoy ? 0 : 1);// 设置背景的层级，切换颜色

        String introduction = card.getIntroduction();
        if (TextUtils.isEmpty(introduction)) {
            mDescription.setText(R.string.default_user_description);
        } else {
            mDescription.setText(introduction);
        }
        mCollapsingToolbarLayout.setTitle(card.getName());
    }

    private boolean myself() {
        return mUserId == UserInfo.id();
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
                if (mUserCard != null) {
                    // not friend then open a dialog to send a request
                    if (!mUserCard.isFriend()) {
                        DialogUtils.createConfirmDialog(this,
                                "发送好友申请",
                                "只有成为好友之后才能发送私信，点击确认向对方发送添加好友申请",
                                "确定",
                                "取消",
                                (dialog, which) -> myPresenter()
                                        .performSendFriendApply(UserInfo.id(), mUserId),
                                null
                        ).show();
                    } else {
                        AppUtils.jumpToChatPage(this, mUserCard);
                    }
                } else {
                    LogUtil.e(TAG, "mUserCard=null!");
                }
                break;
            case R.id.action_edit_mode:
                if (mUserCard != null) {
                    AppUtils.jumpToEditProfilePage(this, mUserCard);
                } else {
                    LogUtil.e(TAG, "mUserCard=null!");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProfile(UserCard user) {
        initOrUpdateProfileUI(user);
        mUserCard = user;
        showLoadingIndicator();
        myPresenter().performPullDynamics(mUserId);
        UserInfo.registerLoginStatusListener(this);
    }

    @Override
    public void showPullDynamicsSuccess(List<DynamicCard> dynamics) {
        ProfileDynamicAdapter adapter
                = new ProfileDynamicAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        adapter.performDataSetAdded(dynamics);
    }

    @Override
    public void showSendApplySuccess() {
        ToastUtils.showToast("发送请求成功.");
    }

    @Override
    public ProfileContract.Presenter myPresenter() {
        if (mUserPresenterImpl == null) {
            mUserPresenterImpl = new UserPresenterImpl(this);
        }
        return mUserPresenterImpl;
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
        ToastUtils.showToast(getResources().getString(R.string.
                tip_info_load_failed) + "：" + message);
    }

    @Override
    public void onUserInfoLoaded(UserCard user) {
        mUserCard = user;
        initOrUpdateProfileUI(mUserCard);
    }

    @Override
    public void onUserInfoCleared() {

    }

    @Override
    public void onUserInfoUpdated(UserCard user) {
        mUserCard = user;
        initOrUpdateProfileUI(mUserCard);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserInfo.unregisterLoginStatusListener(this);
    }
}
