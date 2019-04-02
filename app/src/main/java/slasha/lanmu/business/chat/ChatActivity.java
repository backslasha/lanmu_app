package slasha.lanmu.business.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.util.List;

import slasha.lanmu.GlobalBuffer;
import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.bean.Message;
import slasha.lanmu.bean.User;
import slasha.lanmu.utils.ToastUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class ChatActivity extends SameStyleActivity
        implements View.OnClickListener, View.OnLongClickListener, ChatContract.ChatView {

    private static final String EXTRA_OTHER_USER = "extra_other_user";

    public static Intent newIntent(Context context, User other) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_OTHER_USER, other);
        return intent;
    }

    private RecyclerView mRecyclerView;
    private SimpleAdapter<Message> mMessageAdapter;
    private ChatContract.ChatPresenter mPresenter;
    private User mChatGuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // prepared views
        mRecyclerView = findViewById(R.id.recycler_view);

        // handle Intent
        handleIntent(getIntent());

        if (!GlobalBuffer.AccountInfo.loggedIn()) {
            throw new IllegalArgumentException();
        }

        myPresenter().performPullMessages(
                GlobalBuffer.AccountInfo.currentUser().getId(),
                mChatGuy.getId()
        );

    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        mChatGuy = (User) intent.getSerializableExtra(EXTRA_OTHER_USER);
        setTitle(mChatGuy.getUsername());
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_chat;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_avatar:
                ToastUtils.showToast("you click iv avatar.");
                break;
            case R.id.tv_bubble_username:
                ToastUtils.showToast("you click username.");
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bubble_content:
                ToastUtils.showToast("you long click text content.");
                break;
        }
        return true;
    }


    @Override
    public ChatContract.ChatPresenter myPresenter() {
        if (mPresenter == null) {
            mPresenter = new ChatPresenterImpl(
                    new ChatModelImpl(),
                    this
            );
        }
        return mPresenter;
    }

    @Override
    public void showMessages(List<Message> messages) {
        if (mMessageAdapter == null) {
            mMessageAdapter = new ChatAdapter(this);
            mRecyclerView.setAdapter(mMessageAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        mMessageAdapter.performDataSetChanged(messages);
    }

    public class ChatAdapter extends SimpleAdapter<Message> {

        private byte VIEW_TYPE_FROM_ME = 0x1;
        private byte VIEW_TYPE_FROM_OTHERS = 0x1 << 1;

        ChatAdapter(Context context) {
            super(context);
            if (!GlobalBuffer.AccountInfo.loggedIn()) {
                throw new IllegalArgumentException("init ChatAdapter under un loggedIn status!");
            }
        }

        @Override
        public void bind(@NonNull SimpleHolder holder, @NonNull Message message) {
            User from = message.getFrom();
            if (from != null) {
                holder.setImage(R.id.iv_avatar, from.getAvatarUrl());
                holder.setText(R.id.tv_bubble_username, from.getUsername());
                holder.setOnClickListener(R.id.iv_avatar, ChatActivity.this);
                holder.setOnClickListener(R.id.tv_bubble_username, ChatActivity.this);
            }
            holder.setText(R.id.tv_bubble_content, message.getContent());
            holder.setOnLongClickListener(R.id.tv_bubble_content, ChatActivity.this);
        }

        @Override
        protected int layoutResId(int viewType) {
            if (viewType == VIEW_TYPE_FROM_ME) {
                return R.layout.item_chat_right;
            } else if (viewType == VIEW_TYPE_FROM_OTHERS) {
                return R.layout.item_chat_left;
            }
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            return getItemViewType(getEntities().get(position));
        }

        private int getItemViewType(Message message) {
            User from = message.getFrom();
            if (from.getId() == GlobalBuffer.AccountInfo.currentUser().getId()) {
                return VIEW_TYPE_FROM_ME;
            } else {
                return VIEW_TYPE_FROM_OTHERS;
            }
        }

    }
}
