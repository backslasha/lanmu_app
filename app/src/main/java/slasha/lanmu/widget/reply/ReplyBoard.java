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

import androidx.annotation.Nullable;
import slasha.lanmu.R;
import slasha.lanmu.utils.KeyBoardUtils;

public class ReplyBoard extends LinearLayout implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener {

    private final CharSequence mHint;
    private EditText mEditText;
    private TextView mTextView;
    private OnSendKeyListener mOnSendKeyListener;

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
            if (mOnSendKeyListener != null) {
                mOnSendKeyListener.onSendKeyClick();
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
        if (s.length() > 1) {
            mTextView.setTextColor(
                    getContext().getResources().getColor(R.color.colorClickableText)
            );
            mTextView.setEnabled(true);
        } else {
            mTextView.setTextColor(
                    getContext().getResources().getColor(R.color.colorDefaultText)
            );
            mTextView.setEnabled(false);
        }
    }

    public void open(String hint) {
        mEditText.requestFocus();
        mEditText.setHint(hint);
        KeyBoardUtils.openKeybord(mEditText);
    }

    public void close() {
        mEditText.clearFocus();
        mEditText.setHint(mHint);
        KeyBoardUtils.closeKeybord(mEditText);
    }

    public void clear() {
        mEditText.setText("");
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            KeyBoardUtils.closeKeybord(mEditText);
        }
    }

    public void setOnSendKeyListener(OnSendKeyListener onSendKeyListener) {
        mOnSendKeyListener = onSendKeyListener;
    }

    public String getContent() {
        return String.valueOf(mEditText.getText());
    }

    public interface OnSendKeyListener {
        void onSendKeyClick();
    }

}


