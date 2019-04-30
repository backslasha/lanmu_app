package slasha.lanmu.business.post_detail.apdater;

import android.content.Context;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import slasha.lanmu.R;
import slasha.lanmu.entity.card.CommentCard;
import slasha.lanmu.entity.card.CommentReplyCard;
import slasha.lanmu.utils.common.SpannableStringUtils;
import slasha.lanmu.utils.common.ToastUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class CommentReplyAdapter extends SimpleAdapter<CommentReplyCard> {

    private Context mContext;
    private List<CommentReplyCard> mReplies;
    private int mTotalItemCount;
    private final boolean expandable;
    private onItemClickListener mOnItemClickListener;

    CommentReplyAdapter(@NonNull Context context,
                        @NonNull CommentCard comment) {
        super(context, comment.getReplies());
        mContext = context;
        mReplies = comment.getReplies();
        mTotalItemCount = comment.getReplyCount();
        expandable = mReplies.size() < mTotalItemCount;
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
            return mReplies.size() + 1;
        }
        return mReplies.size();
    }


    @Override
    protected void bind(SimpleHolder holder, CommentReplyCard reply) {
        TextView itemView = (TextView) holder.itemView;
        itemView.setMovementMethod(LinkMovementMethod.getInstance());

        if (reply != null) {
            String colon = mContext.getString(R.string.colon);
            SpannableStringUtils.Builder builder =
                    SpannableStringUtils.getBuilder(reply.getFromName())
                            .setForegroundColor(Color.parseColor("#03A7EB"))
                            .setClickSpan(new ClickableSpan() {
                                @Override
                                public void onClick(@NonNull View widget) {
                                    ToastUtils.showToast("you click me!");
                                }
                            })
                            .append(colon);

            if (reply.getToName() != null) {
                builder.append(mContext.getString(R.string.reply))
                        .append(reply.getToName())
                        .setForegroundColor(
                                mContext.getResources().getColor(R.color.colorClickableText)
                        )
                        .setClickSpan(new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                                ToastUtils.showToast("you click me!");
                            }
                        })
                        .append(colon);
            }

            builder.append(reply.getContent());
            itemView.setText(builder.create());
            itemView.setOnClickListener(v -> {
                if (itemView.getSelectionStart() == -1 && itemView.getSelectionEnd() == -1) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(false, reply);
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
                    mOnItemClickListener.onItemClick(true, null);
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(boolean isExpandableItem, CommentReplyCard reply);
    }
}
