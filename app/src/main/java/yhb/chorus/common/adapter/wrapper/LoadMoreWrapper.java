package yhb.chorus.common.adapter.wrapper;

import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * will not work when setNestedScrollEnabled set to false
 * Created by yhb on 18-2-7.
 */

public class LoadMoreWrapper<Entity> extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_LOADING = Integer.MAX_VALUE - 2;
    private static final int VIEW_TYPE_FINISH_LOADING = Integer.MAX_VALUE - 3;

    private SimpleAdapter<Entity> mInnerAdapter;
    private int mLoadingResId, mFinishedResId;
    private boolean mFinishLoadMore;

    public List<Entity> getEntities() {
        return mInnerAdapter.getEntities();
    }

    public LoadMoreWrapper(SimpleAdapter<Entity> innerAdapter, int loadingResId, int finishedResId) {
        mInnerAdapter = innerAdapter;
        mLoadingResId = loadingResId;
        mFinishedResId = finishedResId;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= mInnerAdapter.getItemCount()) {
            if (mFinishLoadMore) {
                return VIEW_TYPE_FINISH_LOADING;
            }
            return VIEW_TYPE_LOADING;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            return SimpleHolder.createViewHolder(parent.getContext(), parent, mLoadingResId);
        } else if (viewType == VIEW_TYPE_FINISH_LOADING) {
            return SimpleHolder.createViewHolder(parent.getContext(), parent, mFinishedResId);
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_LOADING
                || holder.getItemViewType() == VIEW_TYPE_FINISH_LOADING) {
            // bind nothing
            return;
        }
        mInnerAdapter.onBindViewHolder((SimpleHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + 1;
    }

    public void performDataSetChanged(List<Entity> entities) {
        if (entities == null) {
            notifyItemRangeChanged(0, mInnerAdapter.getEntities().size());
            return;
        }
        mInnerAdapter.getEntities().clear();
        mInnerAdapter.getEntities().addAll(entities);
        notifyDataSetChanged();
    }

    public void performDataSetAdded(List<Entity> entities) {
        if (entities == null) {
            notifyItemRangeChanged(0, mInnerAdapter.getEntities().size());
            return;
        }
        mInnerAdapter.getEntities().addAll(entities);
        notifyDataSetChanged();
    }

    public void performSingleDataAdded(Entity entity) {
        mInnerAdapter.getEntities().add(entity);
        notifyItemInserted(mInnerAdapter.getEntities().size() - 1);
    }

    public void performSingleDataRemoved(Entity entity) {
        int position = mInnerAdapter.getEntities().indexOf(entity);
        if (position != -1) {
            mInnerAdapter.getEntities().remove(entity);
        }
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mInnerAdapter.getEntities().size() - position - 1);
    }

    public void performSingleDataChanged(Entity entity) {
        int position = mInnerAdapter.getEntities().indexOf(entity);
        if (position != -1) {
            mInnerAdapter.getEntities().remove(entity);
            mInnerAdapter.getEntities().add(position, entity);
        }
        notifyItemChanged(position);
    }

    public boolean isFinishLoadMore() {
        return mFinishLoadMore;
    }

    public void setFinishedLoadMore(boolean finishLoadMore) {
        mFinishLoadMore = finishLoadMore;
    }

    public abstract static class OnLoadMoreListener extends RecyclerView.OnScrollListener {

        private int itemCount;
        private int lastItemPosition;
        private boolean allowScroll = false;
        private RecyclerView.LayoutManager layoutManager;

        protected abstract void onLoading(int countItem, int lastItem);

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            //拖拽或者惯性滑动时isScrolled 设置为true
            allowScroll = newState == RecyclerView.SCROLL_STATE_DRAGGING
                    || newState == SCROLL_STATE_SETTLING;
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                layoutManager = recyclerView.getLayoutManager();
                itemCount = layoutManager.getItemCount();
                lastItemPosition = ((LinearLayoutManager) layoutManager)
                        .findLastCompletelyVisibleItemPosition();
            }
            if (allowScroll && itemCount != lastItemPosition && lastItemPosition == itemCount - 1) {
                onLoading(itemCount, lastItemPosition);
            }
        }
    }

}
