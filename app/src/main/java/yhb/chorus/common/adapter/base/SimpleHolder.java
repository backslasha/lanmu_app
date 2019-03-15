package yhb.chorus.common.adapter.base;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by yhb on 17-12-16.
 */

public class SimpleHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;

    private SimpleHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }


    public static SimpleHolder createViewHolder(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new SimpleHolder(context, itemView);
    }

    public static SimpleHolder createViewHolder(Context context, View itemView) {
        return new SimpleHolder(context, itemView);
    }

    /**
     * 通过viewId获取控件
     */
    public View getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }

    public void setText(@IdRes int idRes, String content) {
        TextView textView = (TextView) getView(idRes);
        if (textView != null) {
            textView.setText(content);
        }
    }

    public void setImage(@IdRes int idRes, @DrawableRes int drawableRes) {
        ImageView imageView = (ImageView) getView(idRes);
        if (imageView != null) {
            imageView.setImageResource(drawableRes);
        }
    }

    public void setImage(@IdRes int idRes, String url) {
        Picasso.with(mContext)
                .load(url)
                .into((ImageView) getView(idRes));
    }

    public void setOnClickListener(@IdRes int idRes, View.OnClickListener onClickListener) {
        View view = getView(idRes);
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
    }

    public void setOnLongClickListener(@IdRes int idRes,
                                       View.OnLongClickListener onLongClickListener) {
        View view = getView(idRes);
        if (view != null) {
            view.setOnLongClickListener(onLongClickListener);
        }
    }
}
