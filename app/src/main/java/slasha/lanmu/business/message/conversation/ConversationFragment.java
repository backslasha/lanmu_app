package slasha.lanmu.business.message.conversation;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import slasha.lanmu.BaseFragment;
import slasha.lanmu.R;
import slasha.lanmu.entity.card.ConversationCard;
import slasha.lanmu.entity.card.MessageCard;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.FormatUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class ConversationFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ConversationAdapter mConversationAdapter;

    public static ConversationFragment newInstance() {
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
        
    }


    class ConversationAdapter extends SimpleAdapter<ConversationCard> {

        ConversationAdapter(Context context) {
            super(context);
        }

        @Override
        protected int layoutResId(int viewType) {
            return R.layout.item_conversation;
        }

        @Override
        protected void bind(SimpleHolder holder, ConversationCard conversationCard) {
            MessageCard recentMsg = conversationCard.getRecentMsg();
            UserCard talkTo = conversationCard.getTalkTo();
            holder.setText(R.id.tv_username, talkTo.getName());
            holder.setText(R.id.tv_time, FormatUtils.format1(recentMsg.getTime()));
            holder.setText(R.id.tv_content, recentMsg.getContent());
            CommonUtils.setAvatar((ImageView) holder.getView(R.id.iv_avatar), talkTo.getAvatarUrl());
        }
    }
}

