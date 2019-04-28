package slasha.lanmu.business.post_detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.application.LanmuApplication;
import slasha.lanmu.business.post_detail.apdater.CommentAdapter;
import slasha.lanmu.business.profile.UserProfileActivity;
import slasha.lanmu.entity.card.BookCard;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.entity.local.Comment;
import slasha.lanmu.entity.local.CommentReply;
import slasha.lanmu.entity.local.User;
import slasha.lanmu.persistence.Global;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.common.ToastUtils;
import slasha.lanmu.widget.AppBarStateChangeListener;
import slasha.lanmu.widget.reply.Publisher;
import slasha.lanmu.widget.reply.ReplyBoard;
import slasha.lanmu.widget.reply.ReplyPublisher;
import yhb.chorus.common.adapter.SimpleAdapter;

public class PostDetailActivity extends SameStyleActivity
        implements PostDetailContract.PostDetailView {


    private static final CharSequence EMPTY_TITLE = " ";
    private static final String EXTRA_BOOK_POST = "extra_book_post";
    private RecyclerView mRecyclerViewComments;
    private SwipeRefreshLayout mSwipeRefreshLayoutComments;
    private TextView mTvTitle, mTvDescription, mTvPostContent;
    private ImageView mIvCover;
    private SimpleAdapter<Comment> mAdapter;
    private BookPostCard mBookPost;
    private PostDetailContract.PostDetailPresenter mPostDetailPresenter;
    private ImageView mIvAvatar;
    private CardView mCardView;
    private TextView mTvCreatorName;

    private ReplyBoard mReplyBoard;
    private Publisher.CommentData mCommentData;
    private Publisher.CommentReplyData mCommentReplyData;
    private TextView mTvCommentCount;

    public static Intent newIntent(Context context, BookPostCard bookPost) {
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
        mTvCommentCount = findViewById(R.id.tv_comment_count);

        CollapsingToolbarLayout collapsingToolbarLayout =
                findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(false);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.BLACK);

        AppBarLayout appbarLayout = findViewById(R.id.app_bar_layout);
        appbarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            protected void onCollapsed(AppBarLayout appBarLayout) {
                setTitle(mBookPost.getBook().getName() + " : " +
                        getString(R.string.comments_title));
            }

            @Override
            protected void onScrolled(AppBarLayout appBarLayout) {
                setTitle(mBookPost.getBook().getName());
                mReplyBoard.close();
            }

            @Override
            protected void onExpanded(AppBarLayout appBarLayout) {

            }
        });


        // comments
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewComments = findViewById(R.id.recycler_view);
        mRecyclerViewComments.setLayoutManager(linearLayoutManager);
        mRecyclerViewComments.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mReplyBoard.close();
            }
        });
        mSwipeRefreshLayoutComments = findViewById(R.id.swipe_refresh_layout_comments);
        mSwipeRefreshLayoutComments.setOnRefreshListener(() -> {
            mReplyBoard.close();
            ToastUtils.showToast("onRefreshing...");
            AppUtils.postOnUiThread(
                    () -> mSwipeRefreshLayoutComments.setRefreshing(false),
                    Global.Debug.sLoadingTime
            );
        });


        mReplyBoard = findViewById(R.id.reply_board);
        mReplyBoard.setOnSendKeyListener(() -> {
            if (mCommentReplyData.prepared()) {
                myPresenter().performPublishCommentReply(mCommentReplyData, mReplyBoard.getContent());
                mCommentReplyData.clean();
            } else if (mCommentData.prepared()) {
                myPresenter().performPublishComment(mCommentData, mReplyBoard.getContent());
                mCommentData.clean();
            }
        });

        handleIntent(getIntent());

    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        mBookPost = (BookPostCard) intent.getSerializableExtra(EXTRA_BOOK_POST);
        showDetail(mBookPost);

        setTitle(mBookPost.getBook().getName());

        mCommentData = new Publisher.CommentData(
                mBookPost.getId(),
                UserInfo.self().getId()
        );
        mCommentReplyData = new Publisher.CommentReplyData(
                mBookPost.getId(),
                UserInfo.self().getId()
        );

        myPresenter().performPullComments(mBookPost.getId());
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_post_detail;
    }

    @Override
    public void showDetail(BookPostCard bookPost) {

        if (bookPost == null) {
            return;
        }

        mTvPostContent.setText(bookPost.getContent());

        BookCard book = bookPost.getBook();
        if (book != null) {
            Picasso.with(LanmuApplication.instance())
                    .load(book.getCoverUrl())
                    .into(mIvCover);
            mTvTitle.setText(book.getName());
            mTvDescription.setText(
                    String.format("%s / %s / %s",
                            book.getAuthor(),
                            book.getPublisher(),
                            book.getPublishDate())
            );
            mCardView.setOnClickListener(l -> {
                // TODO: 2019/4/11  jump to complete book info page.
                ToastUtils.showToast("jump to complete book info page.");
            });
        }

        User creator = bookPost.getCreator();
        if (creator != null) {
            Picasso.with(LanmuApplication.instance())
                    .load(creator.getAvatarUrl())
                    .into(mIvAvatar);
            mTvCreatorName.setText(creator.getName());

        }
    }

    @Override
    public void showComments(List<Comment> comments) {
        if (CommonUtils.isEmpty(comments)) {
            ToastUtils.showToast("no comments found!");
        } else {
            if (mAdapter == null) {
                mAdapter = new CommentAdapter(this);

                ((CommentAdapter) mAdapter).setCommentClickListener(
                        new CommentAdapter.CommentClickListener() {
                            @Override
                            public void onContentClick(Comment comment) {
                                mCommentData.clean();
                                mCommentReplyData.prepare(comment.getId(), -1);
                                mReplyBoard.open(getString(R.string.reply_comment));
                            }

                            @Override
                            public void onCommentReplyClick(boolean isExpandableItem,
                                                            CommentReply reply) {
                                if (isExpandableItem) {
                                    ToastUtils.showToast("jump to reply list!");
                                } else {
                                    mCommentData.clean();
                                    mCommentReplyData.prepare(reply.getCommentId(),
                                            reply.getFrom().getId());
                                    mReplyBoard.open(String.format(getString(R.string.reply_to),
                                            reply.getFrom().getName()
                                    ));
                                }
                            }

                            @Override
                            public void onAvatarClick(UserCard user) {
                                AppUtils.jumpToUserProfile(PostDetailActivity.this, user);
                            }
                        }
                );

                mRecyclerViewComments.setAdapter(mAdapter);

            }
            mAdapter.performDataSetChanged(comments);
            int count = comments.size();
            if (count > 0) {
                mTvCommentCount.setText(String.format(String.format("%s%s",
                        getString(R.string.comments_title),
                        getString(R.string.count)
                ), count));
            }
        }
    }

    @Override
    public PostDetailContract.PostDetailPresenter myPresenter() {
        if (mPostDetailPresenter == null) {
            mPostDetailPresenter = new PostDetailPresenterImpl(
                    this,
                    new PostDetailModelImpl(),
                    new ReplyPublisher.ReplyStatusListener() {
                        @Override
                        public void onPublishSucceed() {
                            hideProgressDialog();
                            mReplyBoard.clear();
                            mReplyBoard.close();
                            ToastUtils.showToast(R.string.publish_succeed);
                        }

                        @Override
                        public void onPublishFailed() {
                            hideProgressDialog();
                            ToastUtils.showToast(R.string.publish_failed);
                        }

                        @Override
                        public void onPublishStarted() {
                            showProgressDialog();
                        }
                    }
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

    @Override
    public void showActionFail(String message) {

    }
}
