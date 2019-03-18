package slasha.lanmu.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


public class StickyHeaderItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "StickyHeaderItemDecoration";

    public interface HeaderHelper {
        /**
         * 数据已 group 划分，此方法应返回 position 位置的数据是否为一个 group 的第一条数据
         *
         * @param position 数据在 Adapter 中的 index
         */
        boolean isGroupCaptain(int position);


        /**
         * 此方法应该返回 Adapter 中 position 处的数据，所在的 Group 的 title
         *
         * @param position 数据在 Adapter 中的 index
         */
        String getGroupTitle(int position);

        /**
         * @return Adapter 中数据的个数（不用 State 中的 getItemCount，我们的 ONARecyclerView 有隐蔽的 FooterView，数据有点乱）
         */
        int getItemCount();


    }

    private HeaderHelper mHeaderHelper;
    private Paint mPaintText;
    private Paint mPaintRect;
    private int mRectHeight;
    private int mHeaderTextColor = Color.BLACK;
    private int mHeaderBackgroundColor = Color.TRANSPARENT;
    private int mHeaderTextSize = 60;
    private int mHeaderHeight = 60;
    private int mHeaderTextPaddingStart;
    private boolean mHeaderTextCenterVertical = true;


    public StickyHeaderItemDecoration(@NonNull HeaderHelper headerHelper) {
        super();
        mHeaderHelper = headerHelper;
        mRectHeight = mHeaderHeight;
        mPaintText = new Paint();
        mPaintText.setColor(mHeaderTextColor);
        mPaintText.setTextSize(mHeaderTextSize);
        mPaintText.setAntiAlias(true);
        mPaintText.setDither(true);

        mPaintRect = new Paint();
        mPaintRect.setColor(mHeaderBackgroundColor);
        mPaintRect.setAntiAlias(true);
        mPaintRect.setDither(true);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        drawStickyHeaders(c, parent);
    }

    private void drawStickyHeaders(Canvas c, RecyclerView parent) {

        final int childCount = parent.getChildCount();
        final int rectLeft = parent.getPaddingLeft();
        final int rectRight = parent.getWidth() - parent.getPaddingRight();

        String groupTitle;

        for (int i = 0; i < childCount; i++) {

            View child = parent.getChildAt(i);

            int position = parent.getChildAdapterPosition(child);

            boolean isGroupCaptain = mHeaderHelper.isGroupCaptain(position);

            // 本 Item 不是组内的第一个 Item && 本 Item 还没到达吸顶处，此时本 Item 啥也不用画，continue
            if (!isGroupCaptain && child.getTop() > mRectHeight) {
                continue;
            }

            groupTitle = mHeaderHelper.getGroupTitle(position);

            // 1. 文字在 item 上方时：rectBottom = child.getTop()
            // 2. item 和文字 重合时：rectBottom = mRectHeight
            // 绘制文字的 Rect 区域的 bottom y 坐标
            int rectBottom = Math.max(child.getTop(), mRectHeight);

            // TODO: 2019/3/18 解决marginTop 在 header 之上的问题
//            // 先画头部，中间为 marginTop
//            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
//            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
//                rectBottom -= ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
//            }

            boolean isTopHeader = rectBottom == mRectHeight;

            // 如果有下一个 Item
            if (position < mHeaderHelper.getItemCount() - 1) {
                String nextGroupTitle = mHeaderHelper.getGroupTitle(position + 1);
                // 下一个 Item 头部和本个头部的不一样 && 本 Item 的 bottom 越过 mRectHeight
                if (!nextGroupTitle.equals(groupTitle) && child.getBottom() < rectBottom) {
                    //本个 Item 头部需要往上移动
                    rectBottom = child.getBottom();
                }
            }

            // 文字区域背景
            int rectTop = rectBottom - mRectHeight;

            mPaintText.setTypeface(isTopHeader ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);

            drawHeader(rectLeft, rectTop, rectRight, rectBottom, groupTitle, c);

        }

    }

    private void drawHeader(int left, int top, int right, int bottom, String groupTitle, Canvas c) {

        // 文字区域背景
        c.drawRect(left, top, right, bottom, mPaintRect);

        // Group 文字
        int textHeight = (int) (
                mPaintText.getFontMetrics().bottom - mPaintText.getFontMetrics().top
        );
        int centerOffset = mHeaderTextCenterVertical ? (mRectHeight - textHeight) / 2 : 0;
        c.drawText(
                groupTitle,
                mHeaderTextPaddingStart + left,
                bottom - mPaintText.getFontMetrics().bottom - centerOffset,
                mPaintText
        );

    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int adapterPosition = parent.getChildAdapterPosition(view);
        if (mHeaderHelper.isGroupCaptain(adapterPosition)) {
            outRect.set(0, mRectHeight, 0, 0);
        } else {
            outRect.set(0, 0, 0, 0);
        }
    }

    /*----------------- API -----------------------*/

    public void setHeaderBackgroundColor(int headerBackgroundColor) {
        mHeaderBackgroundColor = headerBackgroundColor;
        mPaintRect.setColor(mHeaderBackgroundColor);
    }

    public void setHeaderTextSize(int headerTextSize) {
        mHeaderTextSize = headerTextSize;
        mPaintText.setTextSize(mHeaderTextSize);
    }

    public void setHeaderHeight(int headerHeight) {
        mHeaderHeight = headerHeight;
    }

    public void setHeaderTextColor(int headerTextColor) {
        mHeaderTextColor = headerTextColor;
        mPaintText.setColor(mHeaderTextColor);
    }

    public void setHeaderTextPaddingStart(int headerTextPaddingStart) {
        mHeaderTextPaddingStart = headerTextPaddingStart;
    }

    public void setHeaderTextCenterVertical(boolean headerTextCenterVertical) {
        mHeaderTextCenterVertical = headerTextCenterVertical;
    }


}
