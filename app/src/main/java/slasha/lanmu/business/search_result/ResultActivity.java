package slasha.lanmu.business.search_result;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.application.LanmuApplication;
import slasha.lanmu.business.create_post.CreatePostActivity;
import slasha.lanmu.business.post_detail.PostDetailActivity;
import slasha.lanmu.entity.card.BookCard;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.common.ToastUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class ResultActivity extends SameStyleActivity implements
        SearchContract.View, android.view.View.OnClickListener {

    private static final String EXTRA_SEARCH_KEYWORD = "search_keyword";
    private SearchContract.SearchPresenter mPresenter;
    private RecyclerView mRecyclerView;
    private SimpleAdapter<BookPostCard> mBookPostAdapter;
    private TextView mTvCreateBookPostGuide;
    private String mKeyword;

    public static Intent newIntent(Context context, String searchKeyword) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra(EXTRA_SEARCH_KEYWORD, searchKeyword);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // catch views
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mBookPostAdapter);
        mTvCreateBookPostGuide = findViewById(R.id.tv_create_book_post_guide);
        mTvCreateBookPostGuide.setOnClickListener(this);

        // handle intent
        handleIntent(getIntent());

    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_search_result;
    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        mKeyword = intent.getStringExtra(EXTRA_SEARCH_KEYWORD);
        setTitle(
                String.format(getResources().getString(R.string.search_result_page_title), mKeyword)
        );

        myPresenter().performQuery(mKeyword);
    }

    @Override
    public SearchContract.SearchPresenter myPresenter() {
        if (mPresenter == null) {
            mPresenter = new SearchPresenterImpl(
                    new SearchModel(),
                    this
            );
        }
        return mPresenter;
    }

    @Override
    public void showLoadingIndicator() {
        showProgressDialog();
    }

    @Override
    public void hideLoadingIndicator() {
        hideProgressDialog();
    }

    @Override
    public void showBookPosts(List<BookPostCard> bookPosts) {
        if (CommonUtils.isEmpty(bookPosts)) {
            showCreateBookPostGuide();
        } else {
            showBookPostList(bookPosts);
        }
    }

    @Override
    public void showActionFail(String message) {
        ToastUtils.showToast(R.string.tip_search_fail);
    }

    private void showBookPostList(List<BookPostCard> bookPosts) {
        if (mBookPostAdapter == null) {
            mBookPostAdapter = new SimpleAdapter<BookPostCard>(
                    ResultActivity.this) {
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
                        CommonUtils.setCover(
                                (ImageView) holder.getView(R.id.iv_cover),
                                book.getCoverUrl());
                        holder.setText(R.id.tv_introduction, book.getIntroduction());
                    }

                    holder.itemView.setOnClickListener(v -> jumpToPostDetail(bookPost));
                }

            };
            mRecyclerView.setAdapter(mBookPostAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setVisibility(android.view.View.VISIBLE);
        }
        mBookPostAdapter.performDataSetChanged(bookPosts);
    }

    private void showCreateBookPostGuide() {
        mRecyclerView.setVisibility(android.view.View.GONE);
        mTvCreateBookPostGuide.setVisibility(android.view.View.VISIBLE);
        mTvCreateBookPostGuide.setText(String.format(
                getString(R.string.create_book_post_guide),
                mKeyword
        ));
    }

    @Override
    public void onClick(android.view.View v) {
        switch (v.getId()) {
            case R.id.tv_create_book_post_guide:
                jumpToCreatePost();
                break;
        }
    }

    private void jumpToCreatePost() {
        startActivity(
                CreatePostActivity.newIntent(this)
        );
    }

    private void jumpToPostDetail(BookPostCard bookPost) {
        startActivity(
                PostDetailActivity.newIntent(this, bookPost)
        );
    }
}
