package slasha.lanmu.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Px;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.CLASS;


public class StickyHeaderItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "StickyHeaderItemDecoration";
    private final HeaderHelper mHeaderHelper;
    private final Paint mPaintText;
    private final Paint mPaintRect;
    private final @ColorInt
    int mHeaderTextColor;
    private final @ColorInt
    int mHeaderBackgroundColor;
    private final @Px
    int mHeaderTextSize;
    private final @Px
    int mHeaderHeight;
    private final @Px
    int mPaddingLeft, mPaddingRight, mPaddingTop, mPaddingBottom;
    private final @Px
    int mHeaderTextPaddingStart;
    private final boolean mHeaderTextCenterVertical;

    private StickyHeaderItemDecoration(@NonNull Context context, @NonNull Builder builder) {
        mHeaderHelper = builder.mHeaderHelper;
        mHeaderTextColor = builder.mHeaderTextColor;
        mHeaderBackgroundColor = builder.mHeaderBackgroundColor;
        mHeaderTextSize = sp2px(context, builder.mHeaderTextSize);
        mHeaderHeight = dp2px(context, builder.mHeaderHeight);
        mHeaderTextPaddingStart = dp2px(context, builder.mHeaderTextPaddingStart);
        mHeaderTextCenterVertical = builder.mHeaderTextCenterVertical;
        mPaddingLeft = dp2px(context, builder.mPaddingLeft);
        mPaddingRight = dp2px(context, builder.mPaddingRight);
        mPaddingTop = dp2px(context, builder.mPaddingTop);
        mPaddingBottom = dp2px(context, builder.mPaddingBottom);

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
        final int rectLeft = parent.getPaddingLeft() + mPaddingLeft;
        final int rectRight = parent.getWidth() - parent.getPaddingRight() - mPaddingRight;

        String groupTitle;

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            boolean isGroupCaptain = mHeaderHelper.isGroupCaptain(position);

            if (!isGroupCaptain) {
                // old captain is moved out, the item become captain
                isGroupCaptain = child.getTop() < mHeaderHeight;
            }

            // only captain item need a header
            if (!isGroupCaptain) {
                continue;
            }

            groupTitle = mHeaderHelper.getGroupTitle(position);

            // 1. 文字在 item 上方时：rectBottom = child.getTop()
            // 2. item 和文字 重合时：rectBottom = mDrawRectHeight
            // 绘制文字的 Rect 区域的 bottom y 坐标
            int rectBottom = Math.max(child.getTop(), mHeaderHeight);

            boolean isCurrentFixedHeader = rectBottom == mHeaderHeight;

            // if new items follow
            if (position < mHeaderHelper.getItemCount() - 1) {
                String nextGroupTitle = mHeaderHelper.getGroupTitle(position + 1);
                final boolean newGroupComes = !nextGroupTitle.equals(groupTitle);
                final boolean currentItemIsMovingOut =
                        child.getBottom() < rectBottom - mPaddingBottom; // 为了推挤时不造成空白1/2
                if (currentItemIsMovingOut && newGroupComes) {
                    rectBottom = child.getBottom() + mPaddingBottom; // 为了推挤时不造成空白2/2
                }
            }

            int rectTop = rectBottom - mHeaderHeight;

            mPaintText.setTypeface(isCurrentFixedHeader ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);

            drawHeader(rectLeft, rectTop, rectRight, rectBottom, groupTitle, c);

        }

    }

    private void drawHeader(int left, int top, int right, int bottom, String groupTitle, Canvas c) {

        // 文字区域背景
        c.drawRect(left + mPaddingLeft,
                top + mPaddingTop,
                right - mPaddingRight,
                bottom - mPaddingBottom, mPaintRect);

        // Group 文字
        int textHeight = (int) (
                mPaintText.getFontMetrics().bottom - mPaintText.getFontMetrics().top
        );
        int centerOffset = mHeaderTextCenterVertical ?
                (mHeaderHeight - mPaddingTop - mPaddingBottom - textHeight) / 2 : 0;
        c.drawText(
                groupTitle,
                mHeaderTextPaddingStart + left,
                bottom - mPaddingBottom - mPaintText.getFontMetrics().bottom - centerOffset,
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
            outRect.set(0, mHeaderHeight, 0, 0);
        } else {
            outRect.set(0, 0, 0, 0);
        }
    }

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

    public static class Builder {

        private HeaderHelper mHeaderHelper;
        private @Dp
        int mHeaderTextColor = Color.BLACK;
        private @ColorInt
        int mHeaderBackgroundColor = Color.WHITE;
        private @Sp
        int mHeaderTextSize = 18;
        private @Dp
        int mHeaderHeight = 36;
        private @Dp
        int mHeaderTextPaddingStart;
        private boolean mHeaderTextCenterVertical = true;
        private @Dp
        int mPaddingLeft, mPaddingRight, mPaddingTop, mPaddingBottom;

        public Builder(HeaderHelper headerHelper) {
            mHeaderHelper = headerHelper;
        }

        /*----------------- API -----------------------*/

        public Builder setHeaderBackgroundColor(int headerBackgroundColor) {
            mHeaderBackgroundColor = headerBackgroundColor;
            return this;
        }

        public Builder setHeaderTextSize(int headerTextSize) {
            mHeaderTextSize = headerTextSize;
            return this;
        }

        public Builder setHeaderHeight(int headerHeight) {
            mHeaderHeight = headerHeight;
            return this;
        }

        public Builder setHeaderTextColor(int headerTextColor) {
            mHeaderTextColor = headerTextColor;
            return this;
        }

        public Builder setHeaderTextPaddingStart(int headerTextPaddingStart) {
            mHeaderTextPaddingStart = headerTextPaddingStart;
            return this;
        }

        public Builder setHeaderTextCenterVertical(boolean headerTextCenterVertical) {
            mHeaderTextCenterVertical = headerTextCenterVertical;
            return this;
        }

        public Builder setPadding(int left, int top, int right, int bottom) {
            mPaddingLeft = left;
            mPaddingRight = right;
            mPaddingTop = top;
            mPaddingBottom = bottom;
            return this;
        }

        public StickyHeaderItemDecoration build(Context context) {
            return new StickyHeaderItemDecoration(
                    context, this
            );
        }

    }

    private static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    private static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    @Documented
    @Retention(CLASS)
    @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE})
    @Dimension(unit = Dimension.DP)
    @interface Dp {
    }

    @Documented
    @Retention(CLASS)
    @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE})
    @Dimension(unit = Dimension.SP)
    @interface Sp {
    }


}
