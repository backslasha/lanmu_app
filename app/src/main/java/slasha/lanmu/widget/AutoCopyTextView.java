package slasha.lanmu.widget;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.common.ToastUtils;

import static android.content.Context.CLIPBOARD_SERVICE;

public class AutoCopyTextView extends AppCompatTextView {


    private final boolean mFlag;

    public AutoCopyTextView(Context context) {
        this(context, null);
    }

    public AutoCopyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoCopyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLongClickable(true);
        setOnLongClickListener(v -> copyToClipBoard());
        mFlag = true;
    }

    @Override
    public void setOnLongClickListener(@Nullable OnLongClickListener l) {
        if (mFlag) {
            throw new IllegalStateException("Auto copy " +
                    "textView is not allowed to add long click listener!");
        }else {
            super.setOnLongClickListener(l);
        }
    }

    private boolean copyToClipBoard() {
        CharSequence text = getText();
        if (CommonUtils.isEmpty(text)) {
            return false;
        }
        ClipboardManager clipboard = (ClipboardManager)
                getContext().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("评论内容", text);
        clipboard.setPrimaryClip(clip);
        ToastUtils.showToast("已复制到剪贴板");
        return true;
    }
}
