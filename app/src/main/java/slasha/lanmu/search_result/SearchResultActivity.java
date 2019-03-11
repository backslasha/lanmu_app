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

import java.util.List;
import java.util.Locale;

import slasha.lanmu.R;
import slasha.lanmu.bean.Book;
import slasha.lanmu.bean.BookPost;
import slasha.lanmu.bean.CreateInfo;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.ToastUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class SearchResultActivity extends AppCompatActivity implements SearchContract.SearchView {

    private static final String EXTRA_SEARCH_KEYWORD = "search_keyword";
    private SearchContract.SearchPresenter mPresenter;
    private RecyclerView mRecyclerView;
    private SimpleAdapter<BookPost> mBookPostAdapter;

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

        // handle intent
        handleIntent(getIntent());

    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        String keyword = intent.getStringExtra(EXTRA_SEARCH_KEYWORD);
        setTitle(
                String.format(getResources().getString(R.string.search_result_page_title), keyword)
        );

        myPresenter().performQuery(keyword);
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
                    }
                    holder.setImage(R.id.iv_cover, R.drawable.cover_sample);
                    if (createInfo != null) {
                        holder.setText(R.id.tv_description, createInfo.getDescription());
                    }
                }
            };
            mRecyclerView.setAdapter(mBookPostAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            mBookPostAdapter.performDataSetChanged(bookPosts);
        }
    }

    private void showCreateBookPostGuide() {
        ToastUtils.showToast(
                "factually i navigate to create book post guide"
        );
    }
}
