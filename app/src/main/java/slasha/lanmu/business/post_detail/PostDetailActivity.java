package slasha.lanmu.business.post_detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ListViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.application.LanmuApplication;
import slasha.lanmu.bean.Book;
import slasha.lanmu.bean.BookPost;
import slasha.lanmu.bean.Comment;
import slasha.lanmu.bean.User;
import slasha.lanmu.business.profile.UserProfileActivity;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.ToastUtils;
import slasha.lanmu.widget.AppBarStateChangeListener;
import slasha.lanmu.widget.StickyHeaderItemDecoration;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class PostDetailActivity extends SameStyleActivity
        implements PostDetailContract.PostDetailView {


    private static final CharSequence EMPTY_TITLE = " ";
    private static final String EXTRA_BOOK_POST = "extra_book_post";
    private RecyclerView mRecyclerViewComments;
    private SwipeRefreshLayout mSwipeRefreshLayoutComments;
    private TextView mTvTitle, mTvDescription, mTvPostContent;
    private ImageView mIvCover;
    private SimpleAdapter<Comment> mAdapter;
    private BookPost mBookPost;
    private PostDetailContract.PostDetailPresenter mPostDetailPresenter;
    private ImageView mIvAvatar;
    private CardView mCardView;
    private TextView mTvCreatorName;

    public static Intent newIntent(Context context, BookPost bookPost) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(EXTRA_BOOK_POST, bookPost);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // handle ui style
        getWindow().setStatusBarColor(
                getResources().getColor(android.R.color.white)
        );
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_less);

        mIvAvatar = findViewById(R.id.iv_avatar);
        mTvCreatorName = findViewById(R.id.tv_username);

        // book info
        mIvCover = findViewById(R.id.iv_cover);
        mTvTitle = findViewById(R.id.tv_title);
        mTvDescription = findViewById(R.id.tv_description);
        mTvPostContent = findViewById(R.id.tv_post_content);
        mCardView = findViewById(R.id.cv_book_info);

        CollapsingToolbarLayout collapsingToolbarLayout =
                findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(EMPTY_TITLE);// "" 将 显示 app label
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);

        AppBarLayout appbarLayout = findViewById(R.id.app_bar_layout);
        appbarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            protected void onCollapsed(AppBarLayout appBarLayout) {
                collapsingToolbarLayout.setTitle(mBookPost.getBook().getName());
            }

            @Override
            protected void onScrolled(AppBarLayout appBarLayout) {
                collapsingToolbarLayout.setTitle(EMPTY_TITLE);
            }
        });

        // comments
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewComments = findViewById(R.id.recycler_view);
        mRecyclerViewComments.setLayoutManager(linearLayoutManager);
        mSwipeRefreshLayoutComments = findViewById(R.id.swipe_refresh_layout_comments);
        mSwipeRefreshLayoutComments.setOnRefreshListener(() -> {
            ToastUtils.showToast("onRefreshing...");
            AppUtils.postOnUiThread(
                    () -> mSwipeRefreshLayoutComments.setRefreshing(false),
                    2000
            );
        });

        handleIntent(getIntent());

    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        mBookPost = (BookPost) intent.getSerializableExtra(EXTRA_BOOK_POST);
        showDetail(mBookPost);

        myPresenter().performPullComments(mBookPost.getId());
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_post_detail;
    }

    @Override
    public void showDetail(BookPost bookPost) {

        if (bookPost == null) {
            return;
        }

        mTvPostContent.setText(bookPost.getContent());

        Book book = bookPost.getBook();
        if (book != null) {
            Picasso.with(LanmuApplication.instance())
                    .load(book.getImages())
                    .into(mIvCover);
            mTvTitle.setText(book.getName());
            mTvDescription.setText(
                    String.format("%s / %s / %s",
                            book.getAuthor(),
                            book.getPublisher(),
                            book.getPublishDate())
            );
            mCardView.setOnClickListener(l->{
                // TODO: 2019/4/11  jump to complete book info page.
                ToastUtils.showToast("jump to complete book info page.");
            });
        }

        User creator = bookPost.getCreator();
        if (creator != null) {
            Picasso.with(LanmuApplication.instance())
                    .load(creator.getAvatarUrl())
                    .into(mIvAvatar);
            mTvCreatorName.setText(creator.getUsername());

        }
    }

    @Override
    public void showComments(List<Comment> comments) {
        if (CommonUtils.isEmpty(comments)) {
            ToastUtils.showToast("no comments found!");
        } else {
            if (mAdapter == null) {
                mAdapter = new SimpleAdapter<Comment>(
                        PostDetailActivity.this) {
                    @Override
                    protected int layoutResId(int viewType) {
                        return R.layout.item_comment;
                    }

                    @Override
                    public void bind(SimpleHolder holder, Comment comment) {
                        holder.setImage(R.id.iv_avatar, comment.getFrom().getAvatarUrl());
                        holder.setText(R.id.tv_username, comment.getFrom().getUsername());
                        holder.setText(R.id.tv_comment_content, comment.getContent());
                        holder.setText(R.id.tv_publish_date, "2007年7月24日");
                        holder.setText(R.id.tv_thumb_up_count, "999");
                        View.OnClickListener listener =
                                v -> jumpToUserProfile(comment.getFrom());
                        holder.getView(R.id.tv_username).setOnClickListener(listener);
                        holder.getView(R.id.iv_avatar).setOnClickListener(listener);

                        RecyclerView recyclerView
                                = (RecyclerView) holder.getView(R.id.recycler_view_replies);
                        recyclerView.setNestedScrollingEnabled(false);
                        recyclerView.setLayoutManager(
                                new LinearLayoutManager(PostDetailActivity.this)
                        );
                        CommentReplyAdapter commentReplyAdapter = new CommentReplyAdapter(
                                PostDetailActivity.this,
                                comment,
                                2,
                                true
                        );
                        commentReplyAdapter.setOnItemClickListener(
                                (CommentReplyAdapter.onItemClickListener) isExpandableItem -> {
                                    if (isExpandableItem) {
                                        ToastUtils.showToast("jump to reply board!");
                                    } else {
                                        ToastUtils.showToast("raise the reply edit view!");
                                    }
                                });
                        recyclerView.setAdapter(commentReplyAdapter);
                    }

                };
                mRecyclerViewComments.setAdapter(mAdapter);

            }
            mAdapter.performDataSetChanged(comments);
        }
    }

    private void jumpToUserProfile(User user) {
        startActivity(
                UserProfileActivity.newIntent(this, user)
        );
    }

    @Override
    public PostDetailContract.PostDetailPresenter myPresenter() {
        if (mPostDetailPresenter == null) {
            mPostDetailPresenter = new PostDetailPresenterImpl(
                    this,
                    new PostDetailModelImpl()
            );
        }
        return mPostDetailPresenter;
    }

    @Override
    public void showLoadingIndicator() {
        mSwipeRefreshLayoutComments.setRefreshing(true);
    }

    @Override
    public void hideLoadingIndicator() {
        mSwipeRefreshLayoutComments.setRefreshing(false);
    }
}
