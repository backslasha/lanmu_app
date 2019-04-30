package slasha.lanmu.business.profile;

import android.content.Context;
import android.view.View;

import java.util.List;

import slasha.lanmu.R;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.entity.card.DynamicCard;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.FormatUtils;
import slasha.lanmu.utils.common.LogUtil;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class ProfileDynamicAdapter extends SimpleAdapter<DynamicCard> {

    private static final String TAG = "lanmu.profile.dynamic";
    private final Context mContext;

    ProfileDynamicAdapter(Context context) {
        super(context);
        mContext = context;
    }

    ProfileDynamicAdapter(Context context, List<DynamicCard> dynamicCards) {
        super(context, dynamicCards);
        mContext = context;
    }


    @Override
    protected int layoutResId(int viewType) {
        switch (viewType) {
            case DynamicCard.TYPE_CREATE_POST:
                return R.layout.item_dynamic_create_post;
            case DynamicCard.TYPE_COMMENT:
                break;
            case DynamicCard.TYPE_COMMENT_REPLY:
                break;
            case DynamicCard.TYPE_THUMB_UP:
                break;
        }
        LogUtil.e(TAG, "unknown viewType -> " + viewType);
        return -1;
    }

    @Override
    protected void bind(SimpleHolder holder, DynamicCard dynamicCard) {
        switch (dynamicCard.getType()) {
            case DynamicCard.TYPE_CREATE_POST:
                bindReplyPost(holder, dynamicCard);
                break;
        }
    }

    // iv_cover,tv_introduction,tv_title,tv_comment_count,tv_date
    private void bindReplyPost(SimpleHolder holder, DynamicCard dynamicCard) {
        BookPostCard card = dynamicCard.getBookPostCard();
        if (card != null) {
            List<String> urls = FormatUtils.asUrlList(card.getImages());
            if (urls.isEmpty()) {
                holder.getView(R.id.iv_cover).setVisibility(View.GONE);
            } else {
                holder.setImage(R.id.iv_cover, urls.get(0));
            }
            holder.setText(R.id.tv_introduction, card.getContent());
            holder.setText(R.id.tv_title, String.format(
                    "《%s》/%s/%s",
                    card.getBook().getName(),
                    card.getBook().getAuthor(),
                    FormatUtils.format(card.getBook().getPublishDate(), "yyyy-MM")
            ));
            holder.setText(R.id.tv_comment_count, String.valueOf(card.getCommentCount()));
            holder.setText(R.id.tv_date, FormatUtils.format(card.getCreateDate(), "MM月\ndd日"));
            holder.itemView.setOnClickListener(v -> AppUtils.jumpToPostDetail(mContext, card));
        }
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
