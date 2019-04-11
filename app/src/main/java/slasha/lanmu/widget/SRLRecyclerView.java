package slasha.lanmu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * 记录 RecyclerView 初始位置，只有从原始位置开始滑动时，能触发 SwipeRefreshLayout 的下拉
 */
public class SRLRecyclerView extends RecyclerView implements ViewTreeObserver.OnGlobalLayoutListener {

    private boolean workWithSwipeRefreshLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mAbsTop;

    public SRLRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public SRLRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SRLRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (workWithSwipeRefreshLayout
                && ev.getAction() == MotionEvent.ACTION_DOWN) {
            mSwipeRefreshLayout.setEnabled(getRawY() == mAbsTop);
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void onGlobalLayout() {
        ViewParent parent = getParent();
        if (parent instanceof SwipeRefreshLayout) {
            workWithSwipeRefreshLayout = true;
            mSwipeRefreshLayout = (SwipeRefreshLayout) parent;
            mAbsTop = getRawY();
        }
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    private final int[] out = new int[2];

    private int getRawY() {
        getLocationOnScreen(out);
        return out[1];
    }
}
