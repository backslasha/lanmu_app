package slasha.lanmu.business.conversation;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import slasha.lanmu.BaseFragment;
import slasha.lanmu.R;
import slasha.lanmu.entity.card.MessageCard;
import slasha.lanmu.entity.card.UserCard;
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

    static ConversationFragment newInstance() {
        ConversationFragment fragment = new ConversationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_coversation;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mConversationAdapter = new ConversationAdapter(getContext()));
        mSwipeRefreshLayout.setOnRefreshListener(() ->
                myPresenter().performPullConversations(UserInfo.id()));
    }

    @Override
    protected void initData() {
        myPresenter().performPullConversations(UserInfo.id());
    }

    @Override
    public void showPullConversationSuccess(List<MessageCard> cards) {
        mConversationAdapter.performDataSetChanged(cards);
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


    class ConversationAdapter extends SimpleAdapter<MessageCard> {

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
        protected void bind(SimpleHolder holder, MessageCard recentMsg) {
            final UserCard talkTo = (recentMsg.getTo().getId() == UserInfo.id()) ?
                    recentMsg.getFrom() : recentMsg.getTo();
            holder.setText(R.id.tv_username, talkTo.getName());
            holder.setText(R.id.tv_time, FlexibleTimeFormat.changeTimeToDesc(
                    recentMsg.getTime().getTime()));
            holder.setText(R.id.tv_content, recentMsg.getContent());
            CommonUtils.setAvatar((ImageView) holder.getView(R.id.iv_avatar), talkTo.getAvatarUrl());
            holder.itemView.setOnClickListener(v -> AppUtils.jumpToChatPage(mContext, talkTo));
        }
    }
}

