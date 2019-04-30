package slasha.lanmu.business.post_detail.apdater;

import android.content.Context;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import slasha.lanmu.R;
import slasha.lanmu.entity.local.Comment;
import slasha.lanmu.entity.local.CommentReply;
import slasha.lanmu.entity.local.User;
import slasha.lanmu.utils.common.SpannableStringUtils;
import slasha.lanmu.utils.common.ToastUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class CommentReplyAdapter extends SimpleAdapter<CommentReply> {

    private Context mContext;
    private final List<CommentReply> mReplies;
    private final int mMaxItemCount, mTotalItemCount;
    private final boolean mExpandable;
    private onItemClickListener mOnItemClickListener;

    public CommentReplyAdapter(@NonNull Context context,
                               @NonNull Comment comment,
                               int maxItemCount,
                               boolean expandable) {
//        super(context, comment.getReplies());
//        mContext = context;
//        mReplies = comment.getReplies();
//        mMaxItemCount = maxItemCount;
//        mExpandable = expandable;
//        mTotalItemCount = comment.getReplyCount();
        super(context);
        mContext = context;
        mReplies = Collections.emptyList();
        mMaxItemCount = maxItemCount;
        mExpandable = expandable;
        mTotalItemCount = 0;

    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
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
                    SpannableStringUtils.getBuilder(from.getName())
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
                        .append(to.getName())
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
                    mOnItemClickListener.onItemClick(false, commentReply);
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
                    mOnItemClickListener.onItemClick(true, commentReply);
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(boolean isExpandableItem, CommentReply reply);
    }
}
