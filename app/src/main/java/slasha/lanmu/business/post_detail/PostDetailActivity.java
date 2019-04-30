package slasha.lanmu.business.post_detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import slasha.lanmu.BaseActivity;
import slasha.lanmu.LoadingProvider;
import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.business.post_detail.apdater.CommentAdapter;
import slasha.lanmu.entity.api.comment.CreateCommentModel;
import slasha.lanmu.entity.card.BookCard;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.entity.card.CommentCard;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.entity.local.CommentReply;
import slasha.lanmu.persistence.Global;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.common.LogUtil;
import slasha.lanmu.utils.common.ToastUtils;
import slasha.lanmu.widget.AppBarStateChangeListener;
import slasha.lanmu.widget.reply.Publisher;
import slasha.lanmu.widget.reply.ReplyBoard;
import yhb.chorus.common.adapter.SimpleAdapter;

public class PostDetailActivity extends SameStyleActivity
        implements PostDetailContract.View {

    private static final String EXTRA_BOOK_POST = "extra_book_post";
    private static final String TAG = "lanmu.detail";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerViewComments;
    @BindView(R.id.swipe_refresh_layout_comments)
    SwipeRefreshLayout mSwipeRefreshLayoutComments;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_description)
    TextView mTvDescription;
    @BindView(R.id.tv_post_content)
    TextView mTvPostContent;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.cv_book_info)
    CardView mCardView;
    @BindView(R.id.tv_username)
    TextView mTvCreatorName;
    @BindView(R.id.tv_comment_count)
    TextView mTvCommentCount;
    @BindView(R.id.reply_board)
    ReplyBoard mReplyBoard;


    private SimpleAdapter<CommentCard> mAdapter;
    private BookPostCard mBookPost;
    private PostDetailContract.Presenter mPostDetailPresenter;

    private Publisher.CommentData mCommentData;
    private Publisher.CommentReplyData mCommentReplyData;

    public static Intent newIntent(Context context, BookPostCard bookPost) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(EXTRA_BOOK_POST, bookPost);
        return intent;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mBookPost = (BookPostCard) bundle.getSerializable(EXTRA_BOOK_POST);
        if (mBookPost == null) {
            ToastUtils.showToast("Open post detail activity with empty book post.");
            LogUtil.e(TAG, "Open post detail activity with empty book post.");
            return false;
        }
        return true;
    }


    @Override
    protected void initWindow() {
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.white));
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.icon_less);

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
        mRecyclerViewComments.setLayoutManager(linearLayoutManager);
        mRecyclerViewComments.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                mReplyBoard.close();
            }
        });
        mSwipeRefreshLayoutComments.setOnRefreshListener(() -> {
            mReplyBoard.close();
            ToastUtils.showToast("onRefreshing...");
            AppUtils.postOnUiThread(
                    () -> mSwipeRefreshLayoutComments.setRefreshing(false),
                    Global.Debug.sLoadingTime
            );
        });
        mReplyBoard.setPublisher(new Publisher() {
            @Override
            public void publishComment(CreateCommentModel model) {
                myPresenter().performPublishComment(model,
                        new CommentLoadingProvider(PostDetailActivity.this));
            }

            @Override
            public void publishCommentReply(CommentReplyData commentReplyData, String content) {
                ToastUtils.showToast("todo publish comment reply!");
            }
        });
    }


    @Override
    protected void initData() {
        myPresenter().performPullComments(mBookPost.getId());
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
        mReplyBoard.setDefaultModel(new CreateCommentModel(mBookPost.getId(), UserInfo.id()));
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
            CommonUtils.setImage(mIvCover, book.getCoverUrl());
            mTvTitle.setText(book.getName());
            mTvCreatorName.setText(bookPost.getCreator().getName());
            mTvDescription.setText(
                    String.format("%s / %s / %s",
                            book.getAuthor(), book.getPublisher(), book.getPublishDate()));
            mCardView.setOnClickListener(l -> {
                // TODO: 2019/4/11  jump to complete book info page.
                ToastUtils.showToast("jump to complete book info page.");
            });
        }

        UserCard creator = bookPost.getCreator();
        if (creator != null) {
            CommonUtils.setAvatar(mIvAvatar, creator.getAvatarUrl());
            mTvCreatorName.setText(creator.getName());
        }
    }

    @Override
    public void showComments(List<CommentCard> comments) {
        if (CommonUtils.isEmpty(comments)) {
            ToastUtils.showToast("no comments found!");
        } else {
            if (mAdapter == null) {
                mAdapter = new CommentAdapter(this);

                ((CommentAdapter) mAdapter).setCommentClickListener(
                        new CommentAdapter.CommentClickListener() {
                            @Override
                            public void onContentClick(CommentCard comment) {
//                                mCommentData.clean();
//                                mCommentReplyData.prepare(comment.getId(), -1);
//                                mReplyBoard.openAndAttach(CreateCommentModel.);
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
//                                    mReplyBoard.openAndAttach(String.format(getString(R.string.reply_to),
//                                            reply.getFrom().getName()
//                                    ));
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
    public void showCreateCommentSuccess(CommentCard card) {
        ToastUtils.showToast(R.string.tip_create_comment_success);
        mReplyBoard.close();
        mAdapter.performSingleDataAdded(card);
    }

    @Override
    public PostDetailContract.Presenter myPresenter() {
        if (mPostDetailPresenter == null) {
            mPostDetailPresenter = new PostDetailPresenterImpl(this, new PostDetailModelImpl());
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
        ToastUtils.showToast(getString(R.string.tip_info_load_failed) + "：" + message);
    }

    private static class CommentLoadingProvider implements LoadingProvider {

        private BaseActivity mBaseActivity;

        public CommentLoadingProvider(BaseActivity baseActivity) {
            mBaseActivity = baseActivity;
        }

        @Override
        public void showLoadingIndicator() {
            mBaseActivity.showProgressDialog();
        }

        @Override
        public void hideLoadingIndicator() {
            mBaseActivity.hideProgressDialog();
        }
    }

}
