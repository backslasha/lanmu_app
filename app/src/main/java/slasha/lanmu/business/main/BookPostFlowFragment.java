package slasha.lanmu.business.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import slasha.lanmu.R;
import slasha.lanmu.application.LanmuApplication;
import slasha.lanmu.entity.card.BookCard;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.entity.response.BookPostFlow;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.common.ToastUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

import static slasha.lanmu.utils.AppUtils.jumpToPostDetail;

public class BookPostFlowFragment extends Fragment
        implements MainContract.MainView, SwipeRefreshLayout.OnRefreshListener {

    private static final String ARGS_BOOK_POST_FLOW = "bookPostFlow";
    private RecyclerView mRecyclerView;
    private SimpleAdapter<BookPostCard> mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MainContract.MainPresenter mMainPresenter;
    private FlowType mFlowType;

    static BookPostFlowFragment newInstance(FlowType flowType) {
        Bundle args = new Bundle();
        BookPostFlowFragment fragment = new BookPostFlowFragment();
        args.putSerializable(ARGS_BOOK_POST_FLOW, flowType);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(
                R.layout.fragment_book_post_flow, container, false);
        mRecyclerView = inflate.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = inflate.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        if (getArguments() != null) {
            mFlowType = (FlowType) getArguments().getSerializable(ARGS_BOOK_POST_FLOW);
        }
        myPresenter().performPullBookPostFlow(mFlowType);
        return inflate;
    }


    @Override
    public void showBookPostFlow(BookPostFlow bookPostFlow) {
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

                    holder.itemView.setOnClickListener(v ->
                            jumpToPostDetail(getActivity(), bookPost)
                    );
                }

            };
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        mAdapter.performDataSetChanged(bookPostFlow.getBookPosts());
    }

    @Override
    public void onRefresh() {
        ToastUtils.showToast("onRefreshing...");
        AppUtils.postOnUiThread(
                () -> mSwipeRefreshLayout.setRefreshing(false),
                2000
        );
    }

    @Override
    public MainContract.MainPresenter myPresenter() {
        if (mMainPresenter == null) {
            mMainPresenter = new MainPresenterImpl(this, new MainModelImpl());
        }
        return mMainPresenter;
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


    enum FlowType implements Serializable {

        SUGGESTION(1, "推荐"),
        MY_TRACK(2, "参与");

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

