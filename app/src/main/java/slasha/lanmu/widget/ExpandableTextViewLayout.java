package slasha.lanmu.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import slasha.lanmu.R;
import slasha.lanmu.utils.DensityUtils;
import slasha.lanmu.utils.LogUtil;

public class ExpandableTextViewLayout extends RelativeLayout {

    private static final String TAG = "ExpandableTextViewLayout";

    private TextView mTextView;
    private TextView mContentView;
    private ViewTreeObserver.OnGlobalLayoutListener mLayoutListener;
    private int mMaxLineCount = Integer.MAX_VALUE;
    private boolean mIsExpand = false;

    public ExpandableTextViewLayout(Context context) {
        this(context, null);
    }

    public ExpandableTextViewLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableTextViewLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child) {
        if (getChildCount() >= 2) {
            throw new IllegalStateException("ExpandableTextViewLayout can host only two direct child");
        }

        if (!(child instanceof TextView)) {
            throw new IllegalStateException("ExpandableTextViewLayout can host only textView");
        }
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (getChildCount() >= 2) {
            throw new IllegalStateException("ExpandableTextViewLayout can host only two direct child");
        }
        if (!(child instanceof TextView)) {
            throw new IllegalStateException("ExpandableTextViewLayout can host only textView");
        }
        super.addView(child, index);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() >= 2) {
            throw new IllegalStateException("ExpandableTextViewLayout can host only two direct child");
        }
        if (!(child instanceof TextView)) {
            throw new IllegalStateException("ExpandableTextViewLayout can host only textView");
        }
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() >= 2) {
            throw new IllegalStateException("ExpandableTextViewLayout can host only two direct child");
        }
        if (!(child instanceof TextView)) {
            throw new IllegalStateException("ExpandableTextViewLayout can host only textView");
        }
        super.addView(child, index, params);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = (TextView) getChildAt(0);
        mLayoutListener = new ContentViewOnGlobalLayoutListener();
        mContentView.getViewTreeObserver().addOnGlobalLayoutListener(mLayoutListener);
//        mContentView.setText(text);
        requestLayout();
        postInvalidate();

        mMaxLineCount = mContentView.getMaxLines();
    }

    private void addToggle() {
        if (mTextView == null) {
            mTextView = new TextView(getContext());
            mTextView.setTextColor(getContext().getResources().getColor(R.color.colorClickableText));
            mTextView.setTextSize(
                    DensityUtils.px2dp(getContext(),
                            mContentView.getTextSize()
                    ));
            mTextView.setOnClickListener(v -> toggle());
            mTextView.setText(getContext().getString(R.string.show_more));
            mTextView.setBackgroundResource(R.drawable.selector_dark_on_press);
        }
        RelativeLayout.LayoutParams lp =
                new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
        lp.addRule(ALIGN_END, mContentView.getId());
        lp.addRule(BELOW, mContentView.getId());
        addView(mTextView, lp);
    }

    private void toggle() {
        if (expandable()) {
            if (mIsExpand) {
                mContentView.setMaxLines(mMaxLineCount);
                mTextView.setText(getContext().getString(R.string.show_more));
            } else {
                mContentView.setMaxLines(Integer.MAX_VALUE);
                mTextView.setText(getContext().getString(R.string.show_less));
            }
            mIsExpand = !mIsExpand;
        }
    }

    private boolean expandable() {
        return mTextView != null && mTextView.getVisibility() != GONE;
    }

    private class ContentViewOnGlobalLayoutListener
            implements ViewTreeObserver.OnGlobalLayoutListener {
        @SuppressLint("ObsoleteSdkInt")
        @Override
        public void onGlobalLayout() {
            if (mContentView.getLineCount() > 0) {
                LogUtil.e(TAG, "行数" + mContentView.getLineCount());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mContentView.getViewTreeObserver()
                            .removeOnGlobalLayoutListener(mLayoutListener);
                } else {
                    mContentView.getViewTreeObserver()
                            .removeGlobalOnLayoutListener(mLayoutListener);
                }
                mLayoutListener = null;
                boolean folded = mContentView.getLineCount() > mContentView.getMaxLines();

                if (folded) {
                    addToggle();
                    LogUtil.e(TAG, "addToggle");
                    mIsExpand = false;
                } else {
                    mIsExpand = true;
                }

            }
        }

    }
}
