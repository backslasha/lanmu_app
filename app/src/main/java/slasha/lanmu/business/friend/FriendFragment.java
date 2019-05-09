package slasha.lanmu.business.friend;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import slasha.lanmu.BaseFragment;
import slasha.lanmu.R;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.common.ToastUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class FriendFragment extends BaseFragment implements FriendContract.View {

    private static final String TAG = "lanmu.friend";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private FriendContract.Presenter mPresenter;
    private FriendAdapter mFriendAdapter;

    static FriendFragment newInstance() {
        Bundle args = new Bundle();
        FriendFragment fragment = new FriendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_refresh_recycler_view;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initData() {
        super.initData();
        mFriendAdapter = new FriendAdapter(getContext());
        mRecyclerView.setAdapter(mFriendAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeRefreshLayout.setOnRefreshListener(() ->
                myPresenter().performPullFriends(UserInfo.id()));
        myPresenter().performPullFriendLocally(UserInfo.id());
    }

    @Override
    public FriendContract.Presenter myPresenter() {
        if (null == mPresenter) {
            mPresenter = new FriendPresenterImpl(this);
        }
        return mPresenter;
    }

    @Override
    public void showActionFail(String message) {
        ToastUtils.showToast(getString(R.string.tip_info_load_failed) + message);
    }

    @Override
    public void showLoadingIndicator() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingIndicator() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showPullFriendsLocallySuccess(List<UserCard> cards) {
        mFriendAdapter.performDataSetChanged(cards);
    }

    @Override
    public void showPullFriendsSuccess(List<UserCard> cards) {
        myPresenter().performSyncFriends2Db(cards);
        mFriendAdapter.performDataSetChanged(cards);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.friend, menu);
        MenuItem item = menu.findItem(R.id.action_new_friend);
        Drawable drawable = item.getIcon();
        if (drawable != null) {
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_friend:
                Context context = getContext();
                if (context != null) {
                    AppUtils.jumpToApplyPage(context);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private static class FriendAdapter extends SimpleAdapter<UserCard> {

        private Context mContext;

        FriendAdapter(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        protected int layoutResId(int viewType) {
            return R.layout.item_friend;
        }

        @Override
        protected void bind(SimpleHolder holder, UserCard userCard) {
            holder.setText(R.id.tv_description, userCard.getIntroduction());
            holder.setText(R.id.tv_username, userCard.getName());
            holder.itemView.setOnClickListener(v ->
                    AppUtils.jumpToUserProfile(mContext, userCard.getId()));
            CommonUtils.setAvatar((ImageView) holder.getView(R.id.iv_avatar),
                    userCard.getAvatarUrl());
        }
    }
}
