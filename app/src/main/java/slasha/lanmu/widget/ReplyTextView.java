package slasha.lanmu.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class ReplyTextView extends AppCompatTextView {

    public ReplyTextView(Context context) {
        this(context, null);
    }

    public ReplyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReplyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawableStateChanged() {
        // 选中链接文字时，拦截点击效果
        if (getSelectionStart() == -1 && getSelectionEnd() == -1) {
            super.drawableStateChanged();
        }
    }
}
