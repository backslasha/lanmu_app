package slasha.lanmu.business.post_detail.apdater;

import android.content.Context;
import android.view.View;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import slasha.lanmu.R;
import slasha.lanmu.bean.Comment;
import slasha.lanmu.bean.CommentReply;
import slasha.lanmu.bean.User;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class CommentAdapter extends SimpleAdapter<Comment> {

    private final Context mContext;

    public CommentAdapter(Context context) {
        super(context);
        mContext = context;
    }

    protected CommentAdapter(Context context, List<Comment> comments) {
        super(context, comments);
        mContext = context;
    }

    private CommentClickListener mCommentClickListener;

    @Override
    protected int layoutResId(int viewType) {
        return R.layout.item_comment;
    }

    @Override
    public void bind(SimpleHolder holder, Comment comment) {
        holder.setImage(R.id.iv_avatar, comment.getFrom().getAvatarUrl());
        holder.setText(R.id.tv_username, comment.getFrom().getUsername());
        holder.setText(R.id.tv_comment_content, comment.getContent());
        holder.setOnClickListener(R.id.tv_comment_content, l -> {
            if (mCommentClickListener != null) {
                mCommentClickListener.onContentClick(comment);
            }
        });

        holder.setText(R.id.tv_publish_date, "2007年7月24日");
        holder.setText(R.id.tv_thumb_up_count, "999");

        final View.OnClickListener listener = v -> {
            if (mCommentClickListener != null)
                mCommentClickListener.onAvatarClick(comment.getFrom());
        };

        holder.getView(R.id.tv_username).setOnClickListener(listener);
        holder.getView(R.id.iv_avatar).setOnClickListener(listener);


        RecyclerView recyclerView
                = (RecyclerView) holder.getView(R.id.recycler_view_replies);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(mContext)
        );

        CommentReplyAdapter commentReplyAdapter = new CommentReplyAdapter(mContext, comment,
                2, true);
        commentReplyAdapter.setOnItemClickListener((isExpandableItem, reply) -> {
            if (mCommentClickListener != null) {
                mCommentClickListener.onCommentReplyClick(isExpandableItem, reply);
            }
        });
        recyclerView.setAdapter(commentReplyAdapter);
    }

    public void setCommentClickListener(CommentClickListener commentClickListener) {
        mCommentClickListener = commentClickListener;
    }

    public interface CommentClickListener {
        void onContentClick(Comment comment);

        void onCommentReplyClick(boolean isExpandableItem, CommentReply reply);

        void onAvatarClick(User user);
    }
}
