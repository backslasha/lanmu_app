package slasha.lanmu.business.post_detail.apdater;

import android.content.Context;
import android.view.View;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import slasha.lanmu.R;
import slasha.lanmu.entity.card.CommentCard;
import slasha.lanmu.entity.card.CommentReplyCard;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.entity.local.Comment;
import slasha.lanmu.entity.local.CommentReply;
import slasha.lanmu.entity.local.User;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.FormatUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class CommentAdapter extends SimpleAdapter<CommentCard> {

    private final Context mContext;

    public CommentAdapter(Context context) {
        super(context);
        mContext = context;
    }

    protected CommentAdapter(Context context, List<CommentCard> comments) {
        super(context, comments);
        mContext = context;
    }

    private CommentClickListener mCommentClickListener;

    @Override
    protected int layoutResId(int viewType) {
        return R.layout.item_comment;
    }

    @Override
    public void bind(SimpleHolder holder, CommentCard comment) {
        holder.setImage(R.id.iv_avatar, comment.getFrom().getAvatarUrl());
        holder.setText(R.id.tv_username, comment.getFrom().getName());
        holder.setText(R.id.tv_comment_content, comment.getContent());
        holder.setOnClickListener(R.id.tv_comment_content, l -> {
            if (mCommentClickListener != null) {
                mCommentClickListener.onContentClick(comment, holder.getAdapterPosition());
            }
        });

        holder.setText(R.id.tv_publish_date, FormatUtils.format1(comment.getTime()));
        holder.setText(R.id.tv_thumb_up_count, "999"); //todo thumb up

        final View.OnClickListener listener = v -> {
            if (mCommentClickListener != null)
                mCommentClickListener.onAvatarClick(comment.getFrom());
        };

        holder.getView(R.id.tv_username).setOnClickListener(listener);
        holder.getView(R.id.iv_avatar).setOnClickListener(listener);


        RecyclerView recyclerView
                = (RecyclerView) holder.getView(R.id.recycler_view_replies);
        if (!CommonUtils.isEmpty(comment.getReplies())) {
            CommentReplyAdapter commentReplyAdapter = new CommentReplyAdapter(mContext, comment);
            commentReplyAdapter.setOnItemClickListener((isExpandableItem, reply) -> {
                if (mCommentClickListener != null) {
                    mCommentClickListener.onCommentReplyClick(isExpandableItem, reply,
                            holder.getAdapterPosition());
                }
            });
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(commentReplyAdapter);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void setCommentClickListener(CommentClickListener commentClickListener) {
        mCommentClickListener = commentClickListener;
    }

    public interface CommentClickListener {
        void onContentClick(CommentCard comment, int position);

        void onCommentReplyClick(boolean isExpandableItem, CommentReplyCard reply, int position);

        void onAvatarClick(UserCard user);
    }
}
