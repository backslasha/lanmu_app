package yhb.chorus.common.adapter;

import android.content.Context;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import yhb.chorus.common.adapter.base.SimpleHolder;


/**
 * Created by yhb on 17-12-16.
 */

public abstract class SimpleAdapter<Entity> extends RecyclerView.Adapter<SimpleHolder> {

    private Context mContext;

    private List<Entity> mEntities;

    protected SimpleAdapter(Context context) {
        mContext = context;
        try {
            //noinspection unchecked
            mEntities = ArrayList.class.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected SimpleAdapter(Context context, List<Entity> entities) {
        mContext = context;
        mEntities = entities != null ? entities : new ArrayList<>();
    }

    @NonNull
    @Override
    public SimpleHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        return SimpleHolder.createViewHolder(mContext, parent, layoutResId(viewType));
    }

    @LayoutRes
    protected abstract int layoutResId(int viewType);

    @Override
    public void onBindViewHolder(@NonNull SimpleHolder holder, int position) {
        if (position < mEntities.size()) {
            bind(holder, mEntities.get(position));
        } else {
            bind(holder, null);
        }
    }

    @Override
    public int getItemCount() {
        return mEntities.size();
    }

    protected abstract void bind(SimpleHolder holder, Entity entity);

    public void performDataSetChanged(List<Entity> entities) {
        if (entities == null) {
            notifyItemRangeChanged(0, mEntities.size());
            return;
        }
        this.mEntities.clear();
        this.mEntities.addAll(entities);
        notifyDataSetChanged();
    }

    public void performDataSetAdded(List<Entity> entities) {
        if (entities == null) {
            notifyItemRangeChanged(0, mEntities.size());
            return;
        }
        this.mEntities.addAll(entities);
        notifyDataSetChanged();
    }

    public void performSingleDataAdded(Entity entity) {
        mEntities.add(entity);
        notifyItemInserted(mEntities.size() - 1);
    }

    public void performSingleDataRemoved(Entity entity) {
        int position = mEntities.indexOf(entity);
        if (position != -1) {
            mEntities.remove(entity);
        }
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mEntities.size() - position - 1);
    }

    public void performSingleDataChanged(Entity entity) {
        int position = mEntities.indexOf(entity);
        if (position != -1) {
            mEntities.remove(entity);
            mEntities.add(position, entity);
        }
        notifyItemChanged(position);
    }

    public List<Entity> getEntities() {
        return mEntities;
    }

}