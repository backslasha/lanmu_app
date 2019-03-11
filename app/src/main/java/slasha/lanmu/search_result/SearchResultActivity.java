package slasha.lanmu.search_result;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import slasha.lanmu.R;
import slasha.lanmu.application.LanmuApplication;
import slasha.lanmu.bean.Book;
import slasha.lanmu.bean.BookPost;
import slasha.lanmu.bean.CreateInfo;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.ToastUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class SearchResultActivity extends AppCompatActivity implements SearchContract.SearchView, View.OnClickListener {

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
        setContentView(R.layout.activity_search_result);

        // handle ui style
        getWindow().setStatusBarColor(
                getResources().getColor(R.color.colorPrimaryDark)
        );
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }

        // catch views
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mBookPostAdapter);
        mTvCreateBookPostGuide = findViewById(R.id.tv_create_book_post_guide);
        mTvCreateBookPostGuide.setOnClickListener(this);

        // handle intent
        handleIntent(getIntent());

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                    SearchResultActivity.this, R.layout.item_book_post) {
                @Override
                public void convert(SimpleHolder holder, BookPost bookPost) {
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

                    holder.itemView.setOnClickListener(v -> jumpToPostDetail());
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
        // TODO: 2019/3/11
        ToastUtils.showToast("jumpToCreatePost");
    }

    private void jumpToPostDetail() {
        // TODO: 2019/3/11
        ToastUtils.showToast("jumpToPostDetail");
    }
}
