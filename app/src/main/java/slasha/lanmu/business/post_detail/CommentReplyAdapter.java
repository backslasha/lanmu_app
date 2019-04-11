package slasha.lanmu.business.post_detail;

import android.content.Context;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import slasha.lanmu.R;
import slasha.lanmu.bean.Comment;
import slasha.lanmu.bean.CommentReply;
import slasha.lanmu.bean.User;
import slasha.lanmu.utils.SpannableStringUtils;
import slasha.lanmu.utils.ToastUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

class CommentReplyAdapter extends SimpleAdapter<CommentReply> {

    private Context mContext;
    private final List<CommentReply> mReplies;
    private final int mMaxItemCount, mTotalItemCount;
    private final boolean mExpandable;
    private onItemClickListener mOnItemClickListener;

    CommentReplyAdapter(@NonNull Context context,
                        @NonNull Comment comment,
                        int maxItemCount,
                        boolean expandable) {
        super(context, comment.getReplies());
        mContext = context;
        mReplies = comment.getReplies();
        mMaxItemCount = maxItemCount;
        mExpandable = expandable;
        mTotalItemCount = comment.getReplyCount();
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
        int itemCount = mReplies.size();
        if (itemCount > mMaxItemCount) {
            itemCount = mMaxItemCount;
        }
        if (mExpandable) {
            itemCount++;
        }
        return itemCount;
    }


    @Override
    protected void bind(SimpleHolder holder, CommentReply commentReply) {
        TextView itemView = (TextView) holder.itemView;
        itemView.setMovementMethod(LinkMovementMethod.getInstance());

        if (commentReply != null) {
            User from = commentReply.getFrom();
            User to = commentReply.getTo();

            if (from == null) {
                return;
            }

            String colon = mContext.getString(R.string.colon);
            SpannableStringUtils.Builder builder =
                    SpannableStringUtils.getBuilder(from.getUsername())
                            .setForegroundColor(Color.parseColor("#03A7EB"))
                            .setClickSpan(new ClickableSpan() {
                                @Override
                                public void onClick(@NonNull View widget) {
                                    ToastUtils.showToast("you click me!");
                                }
                            })
                            .append(colon);

            if (to != null) {
                builder.append(mContext.getString(R.string.reply))
                        .append(to.getUsername())
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

            builder.append(commentReply.getContent());
            itemView.setText(builder.create());
            itemView.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(false);
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
                    mOnItemClickListener.onItemClick(true);
                }
            });
        }
    }

    interface onItemClickListener {
        void onItemClick(boolean isExpandableItem);
    }
}
