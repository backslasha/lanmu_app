package slasha.lanmu.business.search_result;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.application.LanmuApplication;
import slasha.lanmu.bean.Book;
import slasha.lanmu.bean.BookPost;
import slasha.lanmu.bean.CreateInfo;
import slasha.lanmu.business.create_post.CreatePostActivity;
import slasha.lanmu.business.post_detail.PostDetailActivity;
import slasha.lanmu.utils.CommonUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class SearchResultActivity extends SameStyleActivity implements
        SearchContract.SearchView, View.OnClickListener {

    private static final String EXTRA_SEARCH_KEYWORD = "search_keyword";
    private SearchContract.SearchPresenter mPresenter;
    private RecyclerView mRecyclerView;
    private SimpleAdapter<BookPost> mBookPostAdapter;
    private TextView mTvCreateBookPostGuide;
    private String mKeyword;

    public static Intent newIntent(Context context, String searchKeyword) {
        Intent intent = new Intent(context, SearchResultActivity.class);
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
            mPresenter = new SearchPresenter(
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
    public void showBookPosts(List<BookPost> bookPosts) {
        if (CommonUtils.isEmpty(bookPosts)) {
            showCreateBookPostGuide();
        } else {
            showBookPostList(bookPosts);
        }
    }

    private void showBookPostList(List<BookPost> bookPosts) {
        if (mBookPostAdapter == null) {
            mBookPostAdapter = new SimpleAdapter<BookPost>(
                    SearchResultActivity.this) {
                @Override
                protected int layoutResId(int viewType) {
                    return R.layout.item_book_post;
                }

                @Override
                public void bind(SimpleHolder holder, BookPost bookPost) {
                    Book book = bookPost.getBook();
                    CreateInfo createInfo = bookPost.getCreateInfo();
                    if (book != null) {
                        holder.setText(R.id.tv_title, book.getName());
                        holder.setText(R.id.tv_author_name, book.getAuthor());
                        Picasso.with(LanmuApplication.instance())
                                .load(book.getCoverUrl())
                                .into((ImageView) holder.getView(R.id.iv_cover));
                    }

                    if (createInfo != null) {
                        holder.setText(R.id.tv_description, createInfo.getDescription());
                    }

                    holder.itemView.setOnClickListener(v -> jumpToPostDetail(bookPost));
                }

            };
            mRecyclerView.setAdapter(mBookPostAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        mBookPostAdapter.performDataSetChanged(bookPosts);
    }

    private void showCreateBookPostGuide() {
        mRecyclerView.setVisibility(View.GONE);
        mTvCreateBookPostGuide.setVisibility(View.VISIBLE);
        mTvCreateBookPostGuide.setText(String.format(
                getString(R.string.create_book_post_guide),
                mKeyword
        ));
    }

    @Override
    public void onClick(View v) {
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

    private void jumpToPostDetail(BookPost bookPost) {
        startActivity(
                PostDetailActivity.newIntent(this, bookPost)
        );
    }
}
