package slasha.lanmu.business.post_detail.apdater;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import slasha.lanmu.R;
import slasha.lanmu.entity.card.CommentCard;
import slasha.lanmu.entity.card.CommentReplyCard;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.FlexibleTimeFormat;
import slasha.lanmu.utils.common.SpannableStringUtils;
import slasha.lanmu.utils.common.ToastUtils;
import slasha.lanmu.widget.ReplyTextView;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class CommentReplyAdapter extends SimpleAdapter<CommentReplyCard> {

    private Context mContext;
    private int mTotalItemCount;
    private final boolean expandable;
    private onItemClickListener mOnItemClickListener;
    private long commentId;
    private boolean showTime;

    CommentReplyAdapter(@NonNull Context context,
                        @NonNull CommentCard comment) {
        super(context, comment.getReplies());
        mContext = context;
        mTotalItemCount = comment.getReplyCount();
        expandable = getEntities().size() < mTotalItemCount;
        commentId = comment.getId();
    }

    public CommentReplyAdapter(@NonNull Context context) {
        super(context);
        mContext = context;
        mTotalItemCount = -1;
        commentId = -1;
        showTime = true;
        expandable = false;
    }

    void setOnItemClickListener(onItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    protected int layoutResId(int viewType) {
        return R.layout.item_comment_reply;
    }

    @Override
    public int getItemCount() {
        if (expandable) {
            return getEntities().size() + 1;
        }
        return getEntities().size();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void bind(SimpleHolder holder, CommentReplyCard reply) {
        ReplyTextView itemView = (ReplyTextView) holder.itemView;
        itemView.setMovementMethod(LinkMovementMethod.getInstance());

        if (reply != null) {
            String colon = mContext.getString(R.string.colon);
            SpannableStringUtils.Builder builder =
                    SpannableStringUtils.getBuilder(reply.getFromName())
                            .setForegroundColor(mContext.getResources()
                                    .getColor(R.color.colorClickableText))
                            .setClickSpan(new ClickableSpan() {
                                @Override
                                public void onClick(@NonNull View widget) {
                                    AppUtils.jumpToUserProfile(mContext, reply.getFromId());
                                }
                            })
                            .append(colon);

            if (reply.getToName() != null) {
                builder.append(mContext.getString(R.string.reply))
                        .append(reply.getToName())
                        .setForegroundColor(mContext.getResources()
                                .getColor(R.color.colorClickableText))
                        .setClickSpan(new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                                AppUtils.jumpToUserProfile(mContext, reply.getToId());
                            }
                        })
                        .append(colon);
            }

            builder.append(reply.getContent());

            if (showTime) {
                builder.append("  ")
                        .append(FlexibleTimeFormat.changeTimeToDesc(reply.getTime().getTime()))
                        .setForegroundColor(mContext.getResources().getColor(R.color.gray));
            }

            itemView.setText(builder.create());
            itemView.setOnClickListener(v -> {
                if (itemView.getSelectionStart() == -1 && itemView.getSelectionEnd() == -1) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(reply);
                    }
                }
            });

        } else {
            itemView.setText(
                    String.format(
                            mContext.getString(R.string.expand_comment_reply),
                            mTotalItemCount
                    )
            );
            itemView.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onExpandedItemClick(commentId);
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(CommentReplyCard reply);

        void onExpandedItemClick(long commentId);
    }
}
