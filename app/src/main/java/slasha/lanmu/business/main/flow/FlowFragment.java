package slasha.lanmu.business.main.flow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import slasha.lanmu.R;
import slasha.lanmu.entity.card.BookCard;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.utils.CommonUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

import static slasha.lanmu.utils.AppUtils.jumpToPostDetail;

public class FlowFragment extends Fragment
        implements FlowContract.View, SwipeRefreshLayout.OnRefreshListener {

    private static final String ARGS_BOOK_POST_FLOW = "bookPostFlow";
    private RecyclerView mRecyclerView;
    private SimpleAdapter<BookPostCard> mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FlowContract.Presenter mPresenter;
    private FlowType mFlowType;

    static FlowFragment newInstance(FlowType flowType) {
        Bundle args = new Bundle();
        FlowFragment fragment = new FlowFragment();
        args.putSerializable(ARGS_BOOK_POST_FLOW, flowType);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater,
                                          @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        android.view.View inflate = inflater.inflate(
                R.layout.fragment_refresh_recycler_view, container, false);
        mRecyclerView = inflate.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = inflate.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        if (getArguments() != null) {
            mFlowType = (FlowType) getArguments().getSerializable(ARGS_BOOK_POST_FLOW);
        }
        myPresenter().performPullBookPosts(mFlowType);
        return inflate;
    }


    @Override
    public void showBookPosts(List<BookPostCard> bookPostCards) {
        if (mAdapter == null) {
            mAdapter = new SimpleAdapter<BookPostCard>(
                    getContext()) {
                @Override
                protected int layoutResId(int viewType) {
                    return R.layout.item_book_post;
                }

                @Override
                public void bind(SimpleHolder holder, BookPostCard bookPost) {
                    BookCard book = bookPost.getBook();
                    if (book != null) {
                        holder.setText(R.id.tv_title, book.getName());
                        holder.setText(R.id.tv_author_name, book.getAuthor());
                        CommonUtils.setCover((ImageView) holder.getView(R.id.iv_cover),
                                book.getCoverUrl());
                        holder.setText(R.id.tv_introduction, book.getIntroduction());
                        holder.setText(R.id.tv_comment_count,
                                String.valueOf(bookPost.getCommentCount()));
                    }

                    holder.itemView.setOnClickListener(v -> {
                                FragmentActivity context = getActivity();
                                if (context != null)
                                    jumpToPostDetail(context, bookPost.getId());
                            }
                    );
                }

            };
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setVisibility(android.view.View.VISIBLE);
        }
        mAdapter.performDataSetChanged(bookPostCards);
    }

    @Override
    public void onRefresh() {
        myPresenter().performPullBookPosts(mFlowType);
    }

    @Override
    public FlowContract.Presenter myPresenter() {
        if (mPresenter == null) {
            mPresenter = new FlowPresenterImpl(this);
        }
        return mPresenter;
    }

    @Override
    public void showLoadingIndicator() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingIndicator() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showActionFail(String message) {

    }


    public enum FlowType implements Serializable {

        LATEST(1, "最新"),
        HOT(2, "热门");

        private final int mType;
        private final String mName;

        FlowType(int type, String name) {
            mType = type;
            mName = name;
        }

        public int getType() {
            return mType;
        }

        public String getName() {
            return mName;
        }
    }
}

