package slasha.lanmu.business.profile;

import android.content.Context;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import slasha.lanmu.R;
import slasha.lanmu.entity.card.DynamicCard;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.FormatUtils;
import slasha.lanmu.utils.common.LogUtil;
import slasha.lanmu.utils.common.SpannableStringUtils;
import slasha.lanmu.utils.common.ToastUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class ProfileDynamicAdapter extends SimpleAdapter<DynamicCard> {

    private static final String TAG = "lanmu.profile.dynamic";
    private final Context mContext;
    private long mUserId;

    public ProfileDynamicAdapter(Context context, long userId) {
        super(context);
        mContext = context;
        mUserId = userId;
    }

    ProfileDynamicAdapter(Context context, List<DynamicCard> dynamicCards, long userId) {
        super(context, dynamicCards);
        mContext = context;
        mUserId = userId;
    }


    @Override
    protected int layoutResId(int viewType) {
        switch (viewType) {
            case DynamicCard.TYPE_CREATE_POST:
                return R.layout.item_dynamic_create_post;
            case DynamicCard.TYPE_COMMENT:
                return R.layout.item_dynamic_comment;
            case DynamicCard.TYPE_COMMENT_REPLY:
                return R.layout.item_dynamic_comment;
            case DynamicCard.TYPE_THUMB_UP:
                return R.layout.item_dynamic_comment;
        }
        LogUtil.e(TAG, "unknown viewType -> " + viewType);
        return -1;
    }

    @Override
    protected void bind(SimpleHolder holder, DynamicCard dynamicCard) {
        switch (dynamicCard.getType()) {
            case DynamicCard.TYPE_CREATE_POST:
                bindCreatePost(holder, dynamicCard);
                break;
            case DynamicCard.TYPE_COMMENT:
                bindComment(holder, dynamicCard);
                break;
            case DynamicCard.TYPE_COMMENT_REPLY:
                bindReply(holder, dynamicCard);
                break;
            case DynamicCard.TYPE_THUMB_UP:
                bindThumbsUp(holder, dynamicCard);
                break;
        }
    }

    // tv_date,tv_content,iv_reply_content,tv_reply_content,tv_book_info
    private void bindReply(SimpleHolder holder, DynamicCard dynamicCard) {
        TextView tvDate = (TextView) holder.getView(R.id.tv_date);
        tvDate.setText(FormatUtils.format(dynamicCard.getTime(), "MM月\ndd日 "));
        TextView tvAction = (TextView) holder.getView(R.id.tv_action);
        tvAction.setMovementMethod(LinkMovementMethod.getInstance());
        tvAction.setText(SpannableStringUtils
                .getBuilder("回复了")
                .append(dynamicCard.getTo().getName())
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        long userId = dynamicCard.getTo().getId();
                        if (userId != mUserId)
                            AppUtils.jumpToUserProfile(mContext, userId);
                        else
                            ToastUtils.showToast("已经在此页面！");
                    }
                }).append("的评论").create()
        );

        holder.setText(R.id.tv_content, dynamicCard.getContent1());
        holder.getView(R.id.iv_reply_content).setVisibility(View.GONE);
        holder.setText(R.id.tv_reply_content, dynamicCard.getTo().getName()
                + "：" + dynamicCard.getContent2());
        holder.setText(R.id.tv_book_info, FormatUtils.bookInfo(dynamicCard.getBook()));
        holder.itemView.setOnClickListener(v ->
                AppUtils.jumpToPostDetail(mContext, dynamicCard.getPostId()));

    }

    private void bindComment(SimpleHolder holder, DynamicCard dynamicCard) {
        TextView tvDate = (TextView) holder.getView(R.id.tv_date);
        tvDate.setText(FormatUtils.format(dynamicCard.getTime(), "MM月\ndd日 "));
        TextView tvAction = (TextView) holder.getView(R.id.tv_action);
        tvAction.setMovementMethod(LinkMovementMethod.getInstance());
        tvAction.setText(SpannableStringUtils
                .getBuilder("评论了")
                .append(dynamicCard.getTo().getName())
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        long userId = dynamicCard.getTo().getId();
                        if (userId != mUserId)
                            AppUtils.jumpToUserProfile(mContext, userId);
                        else
                            ToastUtils.showToast("已经在此页面！");
                    }
                }).append("创建的书帖").create()
        );

        holder.setText(R.id.tv_content, dynamicCard.getContent1());
        holder.setText(R.id.tv_reply_content, dynamicCard.getTo().getName()
                + "：" + dynamicCard.getContent2());
        holder.setText(R.id.tv_book_info, FormatUtils.bookInfo(dynamicCard.getBook()));
        holder.itemView.setOnClickListener(v ->
                AppUtils.jumpToPostDetail(mContext, dynamicCard.getPostId()));

        ImageView ivCover = (ImageView) holder.getView(R.id.iv_reply_content);
        if (!TextUtils.isEmpty(dynamicCard.getCover())) {
            CommonUtils.setCover(ivCover, dynamicCard.getCover());
        } else {
            ivCover.setVisibility(View.GONE);
        }
    }

    private void bindThumbsUp(SimpleHolder holder, DynamicCard dynamicCard) {
        TextView tvDate = (TextView) holder.getView(R.id.tv_date);
        tvDate.setText(FormatUtils.format(dynamicCard.getTime(), "MM月\ndd日 "));
        TextView tvAction = (TextView) holder.getView(R.id.tv_action);
        tvAction.setMovementMethod(LinkMovementMethod.getInstance());
        tvAction.setText(SpannableStringUtils
                .getBuilder("点赞了")
                .append(dynamicCard.getTo().getName())
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        long userId = dynamicCard.getTo().getId();
                        if (userId != mUserId)
                            AppUtils.jumpToUserProfile(mContext, userId);
                        else
                            ToastUtils.showToast("已经在此页面！");
                    }
                }).append("的评论").create()
        );

        holder.getView(R.id.tv_content).setVisibility(View.GONE);
        holder.getView(R.id.iv_reply_content).setVisibility(View.GONE);
        holder.setText(R.id.tv_reply_content, dynamicCard.getTo().getName()
                + "：" + dynamicCard.getContent2());
        holder.setText(R.id.tv_book_info, FormatUtils.bookInfo(dynamicCard.getBook()));
        holder.itemView.setOnClickListener(v ->
                AppUtils.jumpToPostDetail(mContext, dynamicCard.getPostId()));
    }

    // iv_cover,tv_introduction,tv_title,tv_comment_count,tv_date
    private void bindCreatePost(SimpleHolder holder, DynamicCard dynamicCard) {
        if (dynamicCard.getCover().isEmpty()) {
            holder.getView(R.id.iv_cover).setVisibility(View.GONE);
        } else {
            holder.setImage(R.id.iv_cover, dynamicCard.getCover());
        }
        holder.setText(R.id.tv_introduction, dynamicCard.getContent1());
        holder.setText(R.id.tv_book_info, FormatUtils.bookInfo(dynamicCard.getBook()));
        holder.setText(R.id.tv_date, FormatUtils.format(dynamicCard.getTime(), "MM月\ndd日"));
        holder.setText(R.id.tv_action, "创建了书帖");
        holder.itemView.setOnClickListener(v ->
                AppUtils.jumpToPostDetail(mContext, dynamicCard.getId()));
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getEntities().size()) {
            DynamicCard dynamicCard = getEntities().get(position);
            return dynamicCard.getType();
        }
        return super.getItemViewType(position);
    }

}
