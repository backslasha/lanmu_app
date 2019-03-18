package slasha.lanmu.business.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import slasha.lanmu.R;
import slasha.lanmu.application.LanmuApplication;
import slasha.lanmu.bean.Book;
import slasha.lanmu.bean.BookPost;
import slasha.lanmu.bean.BookPostFlow;
import slasha.lanmu.bean.CreateInfo;
import slasha.lanmu.business.main.delegate.ActionbarDelegate;
import slasha.lanmu.business.main.delegate.DrawerDelegate;
import slasha.lanmu.business.post_detail.PostDetailActivity;
import slasha.lanmu.business.search_result.SearchResultActivity;
import slasha.lanmu.utils.ActivityUtils;
import slasha.lanmu.utils.DensityUtils;
import slasha.lanmu.utils.ToastUtils;
import slasha.lanmu.widget.StickyHeaderItemDecoration;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class MainActivity extends AppCompatActivity implements MainContract.MainView {

    private DrawerDelegate mDrawerDelegate;
    private ActionbarDelegate mActionbarDelegate;
    private MainPresenterImpl mMainPresenter;
    private RecyclerView mRecyclerView;
    private SimpleAdapter<BookPost> mAdapter;

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionbarDelegate = new ActionbarDelegate(this);
        mActionbarDelegate.delegate();

        mDrawerDelegate = new DrawerDelegate(this);
        mDrawerDelegate.delegate();

        mRecyclerView = findViewById(R.id.recycler_view);

        // pull data from somewhere
        myPresenter().performPullBookPostFlow();
    }

    @Override
    public void onBackPressed() {
        if (!mDrawerDelegate.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return mActionbarDelegate.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mActionbarDelegate.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDrawerDelegate.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null) {
            return;
        }
        if ("android.intent.action.SEARCH".equals(intent.getAction())) {
            ToastUtils.showToast("intent.getAction():" + intent.getAction());
            startActivity(
                    SearchResultActivity.newIntent(
                            this,
                            String.valueOf(mActionbarDelegate.getSearchView().getQuery())
                    )
            );
        }
    }

    @Override
    public void showBookPostFlow(BookPostFlow bookPostFlow) {
        if (mAdapter == null) {
            mAdapter = new SimpleAdapter<BookPost>(
                    MainActivity.this) {
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
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setVisibility(View.VISIBLE);
            StickyHeaderItemDecoration stickyHeaderItemDecoration = new StickyHeaderItemDecoration(
                    new ItemDecorationHelper(bookPostFlow)
            );
            stickyHeaderItemDecoration.setHeaderBackgroundColor(
                    getResources().getColor(R.color.colorPrimary)
            );
            stickyHeaderItemDecoration.setHeaderTextSize(
                    DensityUtils.sp2px(this, 18)
            );
            stickyHeaderItemDecoration.setHeaderHeight(
                    DensityUtils.dp2px(MainActivity.this, 24)
            );
            stickyHeaderItemDecoration.setHeaderTextPaddingStart(
                    DensityUtils.dp2px(MainActivity.this, 6)
            );
            stickyHeaderItemDecoration.setHeaderTextColor(
                    Color.WHITE
            );
            mRecyclerView.addItemDecoration(stickyHeaderItemDecoration);
        }
        mAdapter.performDataSetChanged(bookPostFlow.getBookPosts());
    }

    private void jumpToPostDetail(BookPost bookPost) {
        startActivity(
                PostDetailActivity.newIntent(this, bookPost)
        );
    }

    @Override
    public MainContract.MainPresenter myPresenter() {
        if (mMainPresenter == null) {
            mMainPresenter = new MainPresenterImpl(this, new MainModelImpl());
        }
        return mMainPresenter;
    }

    private class ItemDecorationHelper implements StickyHeaderItemDecoration.HeaderHelper {

        private BookPostFlow mBookPostFlow;
        private List<BookPost> mBookPosts;
        private final int[] mCounts = new int[3];
        private final String[] mTitles = {
                "热门帖子", "推荐帖子", "浏览历史", "值得一看"
        };

        private ItemDecorationHelper(BookPostFlow bookPostFlow) {
            mBookPostFlow = bookPostFlow;
            mBookPosts = mBookPostFlow.getBookPosts();
            mCounts[0] = mBookPostFlow.getHotCount();
            mCounts[1] = mBookPostFlow.getSuggestCount();
            mCounts[2] = mBookPostFlow.getHistoryCount();
        }

        @Override
        public boolean isGroupCaptain(int position) {
            int indexBase1 = position + 1;
            return 1 == indexBase1
                    || mCounts[0] + 1 == indexBase1
                    || mCounts[0] + mCounts[1] + 1 == indexBase1;
        }

        @Override
        public String getGroupTitle(int position) {
            int indexBase1 = position + 1;
            if (indexBase1 <= mCounts[0]) {
                return mTitles[0];
            } else if (indexBase1 <= mCounts[0] + mCounts[1]) {
                return mTitles[1];
            } else if (indexBase1 <= mCounts[0] + mCounts[1] + mCounts[2]) {
                return mTitles[2];
            }
            return mTitles[3];
        }

        @Override
        public int getItemCount() {
            return mBookPosts == null ? 0 : mBookPosts.size();
        }
    }

}
