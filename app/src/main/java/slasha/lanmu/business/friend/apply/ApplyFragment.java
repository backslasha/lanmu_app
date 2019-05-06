package slasha.lanmu.business.friend.apply;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import slasha.lanmu.BaseActivity;
import slasha.lanmu.BaseFragment;
import slasha.lanmu.LoadingProvider;
import slasha.lanmu.R;
import slasha.lanmu.entity.card.ApplyCard;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.common.ToastUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class ApplyFragment extends BaseFragment
        implements ApplyContract.View {

    public static ApplyFragment newInstance() {
        Bundle args = new Bundle();
        ApplyFragment fragment = new ApplyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ApplyContract.Presenter mPresenter;
    private ApplyAdapter mApplyAdapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_refresh_recycler_view;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mApplyAdapter = new ApplyAdapter(getContext()));
        mSwipeRefreshLayout.setOnRefreshListener(() ->
                myPresenter().performPullApplies(UserInfo.id()));
    }

    @Override
    protected void initData() {
        super.initData();
        myPresenter().performPullApplies(UserInfo.id());
    }

    @Override
    public void showPullAppliesSuccess(List<ApplyCard> cards) {
        mApplyAdapter.performDataSetChanged(cards);
    }

    @Override
    public void showAddFriendSuccess(UserCard userCard) {
        Object tag = mRecyclerView.getTag();
        if (tag instanceof Integer) {
            int position = (int) tag;
            ApplyCard oldCard = mApplyAdapter.getEntities().get(position);
            oldCard.setHandle(1);
            mApplyAdapter.notifyItemChanged(position);
        }
        ToastUtils.showToast(String.format("已添加%s为好友", userCard.getName()));
    }

    @Override
    public void showRejectApplySuccess(ApplyCard applyCard) {
        Object tag = mRecyclerView.getTag();
        if (tag instanceof Integer) {
            int position = (int) tag;
            ApplyCard oldCard = mApplyAdapter.getEntities().get(position);
            oldCard.setHandle(applyCard.getHandle());
            mApplyAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public ApplyContract.Presenter myPresenter() {
        if (mPresenter == null) {
            mPresenter = new ApplyPresenterImpl(this);
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


    private class ApplyAdapter extends SimpleAdapter<ApplyCard> {

        private Context mContext;

        ApplyAdapter(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        protected int layoutResId(int viewType) {
            return R.layout.item_apply;
        }

        @Override
        protected void bind(SimpleHolder holder, ApplyCard applyCard) {
            UserCard userCard = applyCard.getFrom();
            holder.setText(R.id.tv_username, userCard.getName());

            ImageView ivAvatar = (ImageView) holder.getView(R.id.iv_avatar);
            CommonUtils.setAvatar(ivAvatar, userCard.getAvatarUrl());
            ivAvatar.setOnClickListener(v ->
                    AppUtils.jumpToUserProfile(mContext, applyCard.getFromId()));

            TextView tvAgree = (TextView) holder.getView(R.id.tv_agree);
            TextView tvIgnore = (TextView) holder.getView(R.id.tv_ignore);
            TextView tvResult = (TextView) holder.getView(R.id.tv_handle_result);

            if (applyCard.getHandle() == 0) { // un handled
                tvAgree.setVisibility(View.VISIBLE);
                tvIgnore.setVisibility(View.VISIBLE);
                tvResult.setVisibility(View.GONE);
                tvAgree.setOnClickListener(v -> {
                    myPresenter().performAddFriend(UserInfo.id(), userCard.getId(), new HardLoading());
                    mRecyclerView.setTag(holder.getAdapterPosition());
                });
                tvIgnore.setOnClickListener(v -> {
                    myPresenter().performRejectApply(applyCard.getId(), new HardLoading());
                    mRecyclerView.setTag(holder.getAdapterPosition());
                });

            } else if (applyCard.getHandle() == -1) { // rejected
                tvAgree.setVisibility(View.GONE);
                tvIgnore.setVisibility(View.GONE);
                tvResult.setVisibility(View.VISIBLE);
                tvResult.setText(getString(R.string.already_rejected));

            } else { // accepted
                tvAgree.setVisibility(View.GONE);
                tvIgnore.setVisibility(View.GONE);
                tvResult.setVisibility(View.VISIBLE);
                tvResult.setText(getString(R.string.already_agreed));
            }

        }
    }

    private class HardLoading implements LoadingProvider {

        private BaseActivity activity;

        HardLoading() {
            activity = (BaseActivity) getActivity();
        }

        @Override
        public void showLoadingIndicator() {
            activity.showProgressDialog();
        }

        @Override
        public void hideLoadingIndicator() {
            activity.hideProgressDialog();
        }
    }

}
