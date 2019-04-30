package slasha.lanmu.widget.reply;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import slasha.lanmu.R;
import slasha.lanmu.entity.api.comment.CreateCommentModel;
import slasha.lanmu.utils.common.KeyBoardUtils;

public class ReplyBoard extends LinearLayout implements View.OnClickListener,
        TextWatcher, View.OnFocusChangeListener {

    private final CharSequence mHint;
    private EditText mEditText;
    private TextView mTextView;
    private Publisher mPublisher;
    private CreateCommentModel mCommentModel;

    private CreateCommentModel mDefaultModel = new CreateCommentModel();

    public ReplyBoard(Context context) {
        this(context, null);
    }

    public ReplyBoard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReplyBoard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);

    }

    @SuppressLint("ClickableViewAccessibility")
    public ReplyBoard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.view_reply_board, this, true);
        mEditText = findViewById(R.id.edt_reply_area);
        mTextView = findViewById(R.id.tv_publish);
        mEditText.addTextChangedListener(this);
        mEditText.setOnFocusChangeListener(this);
        mTextView.setOnClickListener(this);
        mHint = mEditText.getHint();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_publish) {
            if (mCommentModel == null) {
                mDefaultModel.setContent(getContent());
                mPublisher.publishComment(mDefaultModel);
            } else {
                mCommentModel.setContent(getContent());
                mPublisher.publishComment(mCommentModel);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            mTextView.setTextColor(getContext().getResources().getColor(R.color.colorClickableText));
            mTextView.setEnabled(true);
        } else {
            mTextView.setTextColor(getContext().getResources().getColor(R.color.colorDefaultText));
            mTextView.setEnabled(false);
        }
    }

    public void openAndAttach(Object tag, @NonNull CreateCommentModel model) {
//        CreateCommentModel createCommentModel = cache.get(tag);
//        if (createCommentModel == null) {
//            createCommentModel = model;
//        }
//        String fromName = createCommentModel.getFromName();
//        String content = createCommentModel.getContent();
//        mEditText.setHint(String.format(getContext().getString(R.string.reply_to), fromName));
//        mEditText.setText(content);
//        mEditText.requestFocus();
//        KeyBoardUtils.openKeybord(mEditText);
    }

    public void close() {
        mEditText.clearFocus();
    }

    public void clear() {
        mDefaultModel.setContent("");
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            KeyBoardUtils.closeKeybord(mEditText);
            mDefaultModel.setContent(getContent());
            mEditText.setText("");
        } else {
            mEditText.setText(mDefaultModel.getContent());
        }
    }

    private String getContent() {
        return String.valueOf(mEditText.getText());
    }


    public void setDefaultModel(@NonNull CreateCommentModel model) {
        mDefaultModel.setFromId(model.getFromId());
        mDefaultModel.setPostId(model.getPostId());
    }

    public void setPublisher(@NonNull Publisher publisher) {
        mPublisher = publisher;
    }
}


