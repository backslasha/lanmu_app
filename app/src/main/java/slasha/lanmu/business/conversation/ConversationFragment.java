package slasha.lanmu.business.conversation;


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
import slasha.lanmu.BaseFragment;
import slasha.lanmu.R;
import slasha.lanmu.entity.api.base.UnreadModel;
import slasha.lanmu.entity.card.MessageCard;
import slasha.lanmu.entity.card.UnreadMessagesCard;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.persistence.UnreadInfo;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.FlexibleTimeFormat;
import slasha.lanmu.utils.common.ToastUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class ConversationFragment extends BaseFragment
        implements ConversationContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ConversationAdapter mConversationAdapter;
    private ConversationContract.Presenter mPresenter;
    private boolean mHasSync = false;

    static ConversationFragment newInstance() {
        ConversationFragment fragment = new ConversationFragment();
        Bundle args = new Bundle();
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mConversationAdapter = new ConversationAdapter(getContext()));
        mSwipeRefreshLayout.setOnRefreshListener(() ->
                myPresenter().performPullUnreadMessages(UserInfo.id()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mHasSync) {
            myPresenter().performPullConversations(UserInfo.id(), false);
        } else {
            myPresenter().performPullConversations(UserInfo.id(), true);
            mHasSync = true;
        }
    }

    @Override
    public void showPullConversationLocallySuccess(List<UnreadModel<MessageCard>> cards) {
        UnreadInfo.setMessageCount(0); // mark all unread messages has been noticed by user
        mConversationAdapter.performDataSetChanged(cards);
    }

    @Override
    public void showPullUnreadMessagesSuccess(List<UnreadMessagesCard> cards) {
        if (cards.size() == 0) {
            ToastUtils.showToast("暂无新消息");
            return;
        }
        // 数据库未读消息以经更新，重新拉取
        myPresenter().performPullConversations(UserInfo.id(), false);
    }

    @Override
    public ConversationContract.Presenter myPresenter() {
        if (mPresenter == null) {
            mPresenter = new ConversationPresenterImpl(this);
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


    class ConversationAdapter extends SimpleAdapter<UnreadModel<MessageCard>> {

        private Context mContext;

        ConversationAdapter(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        protected int layoutResId(int viewType) {
            return R.layout.item_conversation;
        }

        @Override
        protected void bind(SimpleHolder holder, UnreadModel<MessageCard> unreadMessagesCard) {
            MessageCard recentMsg = unreadMessagesCard.getResult().get(0);
            final UserCard talkTo = (recentMsg.getTo().getId() == UserInfo.id()) ?
                    recentMsg.getFrom() : recentMsg.getTo();
            holder.setText(R.id.tv_username, talkTo.getName());
            holder.setText(R.id.tv_time, FlexibleTimeFormat.changeTimeToDesc(
                    recentMsg.getTime().getTime()));
            TextView tvUnread = (TextView) holder.getView(R.id.tv_unread_count);
            int unreadCount = unreadMessagesCard.getUnreadCount();
            if (unreadCount > 0) {
                tvUnread.setText(String.valueOf(Math.min(unreadCount, 99)));
                tvUnread.setVisibility(View.VISIBLE);
            }else {
                tvUnread.setVisibility(View.GONE);
            }
            holder.setText(R.id.tv_content, recentMsg.getContent());
            CommonUtils.setAvatar((ImageView) holder.getView(R.id.iv_avatar), talkTo.getAvatarUrl());
            holder.itemView.setOnClickListener(v -> AppUtils.jumpToChatPage(mContext, talkTo));
        }
    }
}

