package slasha.lanmu.business.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.entity.api.message.CreateMsgModel;
import slasha.lanmu.entity.card.MessageCard;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.common.KeyBoardUtils;
import slasha.lanmu.utils.common.ToastUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class ChatActivity extends SameStyleActivity
        implements ChatContract.View {

    private static final String EXTRA_OTHER_USER = "extra_other_user";
    private static final String TAG = "lanmu.chat";

    public static Intent newIntent(Context context, UserCard other) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_OTHER_USER, other);
        return intent;
    }

    @BindView(R.id.edt_input)
    EditText mEdtInput;

    @BindView(R.id.tv_send)
    TextView mTvSend;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    private SimpleAdapter<MessageCard> mMessageAdapter;
    private ChatContract.Presenter mPresenter;
    private UserCard mTalkTo;

    @Override
    protected boolean initArgs(Bundle bundle) {
        mTalkTo = (UserCard) bundle.getSerializable(EXTRA_OTHER_USER);
        if (mTalkTo == null) {
            Log.e(TAG, "Open chat activity without talkTo.");
            ToastUtils.showToast("Open chat activity without talkTo.");
            return false;
        }
        setTitle(mTalkTo.getName());
        return true;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTvSend.setVisibility(View.GONE);
        mEdtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvSend.setEnabled(s.length() > 0);
                if (mTvSend.isEnabled()) {
                    mTvSend.setVisibility(View.VISIBLE);
                } else {
                    mTvSend.setVisibility(View.GONE);
                }
            }
        });
        mEdtInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mEdtInput.setText((CharSequence) mEdtInput.getTag());
            } else {
                mEdtInput.setTag(mEdtInput.getText());
                mEdtInput.setText(null);
                KeyBoardUtils.closeKeybord(mEdtInput);
            }
        });
        mMessageAdapter = new ChatAdapter(this);
        mRecyclerView.setAdapter(mMessageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (mEdtInput.hasFocus()) mEdtInput.clearFocus();
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    @OnClick(R.id.tv_send)
    void send() {
        CreateMsgModel model = new CreateMsgModel();
        model.setContent(String.valueOf(mEdtInput.getText()));
        model.setFromId(UserInfo.id());
        model.setToId(mTalkTo.getId());
        model.setType(MessageCard.TYPE_TEXT);
        myPresenter().performSendMessage(model);
    }

    @Override
    protected void initData() {
//        PullMsgModel model = new PullMsgModel();
//        model.setFromId(UserInfo.id());
//        model.setToId(mTalkTo.getId());
//        model.setPullCount(20);
        myPresenter().performPullMessagesLocally(mTalkTo.getId());
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_chat;
    }


    @Override
    public ChatContract.Presenter myPresenter() {
        if (mPresenter == null) {
            mPresenter = new ChatPresenterImpl(this);
        }
        return mPresenter;
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
        ToastUtils.showToast(getResources().getString(R.string.tip_info_load_failed)
                + "：" + message);
    }

    @Override
    public void showMessages(List<MessageCard> messages) {
        mMessageAdapter.performDataSetChanged(messages);
        mRecyclerView.smoothScrollToPosition(messages.size());
    }

    @Override
    public void showSendMsgSuccess(MessageCard card) {
        mMessageAdapter.performSingleDataAdded(card);
        mRecyclerView.smoothScrollToPosition(mMessageAdapter.getItemCount());
        mEdtInput.setTag(null);
        mEdtInput.setText(null);
    }

    @Override
    public void showSendMsgFail(String info) {
        ToastUtils.showToast(getResources().getString(R.string.tip_send_fail) + info);
    }


    public class ChatAdapter extends SimpleAdapter<MessageCard> {

        private byte VIEW_TYPE_FROM_ME = 0x1;
        private byte VIEW_TYPE_FROM_OTHERS = 0x1 << 1;

        ChatAdapter(Context context) {
            super(context);
        }

        @Override
        public void bind(@NonNull SimpleHolder holder, @NonNull MessageCard message) {
            UserCard from = message.getFrom();
            if (from != null) {
                holder.setImage(R.id.iv_avatar, from.getAvatarUrl());
                holder.setText(R.id.tv_bubble_username, from.getName());
                holder.setOnClickListener(R.id.iv_avatar,
                        v -> AppUtils.jumpToUserProfile(ChatActivity.this, from.getId()));
                holder.setOnClickListener(R.id.tv_bubble_username,
                        v -> AppUtils.jumpToUserProfile(ChatActivity.this, from.getId()));
            }
            holder.setText(R.id.tv_bubble_content, message.getContent());
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

        private int getItemViewType(MessageCard message) {
            UserCard from = message.getFrom();
            if (from.getId() == UserInfo.id()) {
                return VIEW_TYPE_FROM_ME;
            } else {
                return VIEW_TYPE_FROM_OTHERS;
            }
        }
    }
}
