package slasha.lanmu.business.notification;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import slasha.lanmu.BaseFragment;
import slasha.lanmu.R;
import slasha.lanmu.entity.api.comment.NotifyRspModel;
import slasha.lanmu.entity.card.NotifyCard;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.FlexibleTimeFormat;
import slasha.lanmu.utils.FormatUtils;
import slasha.lanmu.utils.common.ToastUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class NotificationFragment extends BaseFragment implements NotificationContract.View {

    private static final String TAG = "lanmu.notification";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private NotificationContract.Presenter mPresenter;
    private NotificationAdapter mNotificationAdapter;

    static NotificationFragment newInstance() {
        Bundle args = new Bundle();
        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_refresh_recycler_view;
    }

    @Override
    protected void initData() {
        super.initData();
        mNotificationAdapter = new NotificationAdapter(getContext());
        mRecyclerView.setAdapter(mNotificationAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeRefreshLayout.setOnRefreshListener(() ->
                myPresenter().performPullNotifications(UserInfo.id()));
        myPresenter().performPullNotifications(UserInfo.id());
    }

    @Override
    public NotificationContract.Presenter myPresenter() {
        if (null == mPresenter) {
            mPresenter = new NotificationPresenterImpl(this);
        }
        return mPresenter;
    }

    @Override
    public void showPullNotificationsSuccess(NotifyRspModel notifyRspModel) {
        mNotificationAdapter.performDataSetChanged(notifyRspModel.getNotifyCards());
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

    private static class NotificationAdapter extends SimpleAdapter<NotifyCard> {

        private Context mContext;

        NotificationAdapter(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        protected int layoutResId(int viewType) {
            switch (viewType) {
                case NotifyCard.TYPE_NEW_COMMENT:
                case NotifyCard.TYPE_NEW_REPLY:
                case NotifyCard.TYPE_NEW_THUMBS_UP:
                    return R.layout.item_notify_new_comment;
            }
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            NotifyCard notifyCard = getEntities().get(position);
            return notifyCard.getType();
        }

        @Override
        protected void bind(SimpleHolder holder, NotifyCard notifyCard) {
            UserCard user = notifyCard.getFrom();
            CommonUtils.setAvatar((ImageView) holder.getView(R.id.iv_avatar), user.getAvatarUrl());
            holder.setText(R.id.tv_username, user.getName());
            holder.setText(R.id.tv_date, FlexibleTimeFormat.changeTimeToDesc(notifyCard.getTime().getTime()));
            holder.setText(R.id.tv_book_info, String.format(
                    "《%s》/%s/%s",
                    notifyCard.getBook().getName(),
                    notifyCard.getBook().getAuthor(),
                    FormatUtils.format(notifyCard.getBook().getPublishDate(), "yyyy-MM")
            ));
            holder.setText(R.id.tv_content, notifyCard.getContent1());

            final View ivReplyContent = holder.getView(R.id.iv_reply_content);
            final View tvReply = holder.getView(R.id.tv_reply);
            if (notifyCard.getType() == NotifyCard.TYPE_NEW_COMMENT) {
                holder.setText(R.id.tv_reply_content, notifyCard.getContent2());
                tvReply.setVisibility(View.VISIBLE);
                tvReply.setOnClickListener(v -> {
                    ToastUtils.showToast("reply!");
                });
                ivReplyContent.setVisibility(View.VISIBLE);
                CommonUtils.setCover((ImageView) ivReplyContent, notifyCard.getCover());
            } else if (notifyCard.getType() == NotifyCard.TYPE_NEW_REPLY) {
                holder.setText(R.id.tv_reply_content, "我：" + notifyCard.getContent2());
                ivReplyContent.setVisibility(View.GONE);
                tvReply.setVisibility(View.VISIBLE);
                tvReply.setOnClickListener(v -> {
                    ToastUtils.showToast("reply2!");
                });
            } else {
                holder.setText(R.id.tv_reply_content, "我：" + notifyCard.getContent2());
                ivReplyContent.setVisibility(View.GONE);
                tvReply.setVisibility(View.INVISIBLE);
            }
            holder.itemView.setOnClickListener(v ->
                    AppUtils.jumpToPostDetail(mContext, notifyCard.getPostId()));
        }
    }
}
