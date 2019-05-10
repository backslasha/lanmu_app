package slasha.lanmu.business.post_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;
import slasha.lanmu.LoadingProvider;
import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.business.post_detail.apdater.CommentAdapter;
import slasha.lanmu.entity.api.base.PageModel;
import slasha.lanmu.entity.api.comment.CreateCommentModel;
import slasha.lanmu.entity.api.comment.CreateReplyModel;
import slasha.lanmu.entity.card.BookCard;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.entity.card.CommentCard;
import slasha.lanmu.entity.card.CommentReplyCard;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.FormatUtils;
import slasha.lanmu.utils.common.LogUtil;
import slasha.lanmu.utils.common.ToastUtils;
import slasha.lanmu.widget.reply.Publisher;
import slasha.lanmu.widget.reply.ReplyBoard;
import yhb.chorus.common.adapter.wrapper.LoadMoreWrapper;

import static slasha.lanmu.entity.card.CommentCard.ORDER_COMMENT_THUMBS_UP_FIRST;
import static slasha.lanmu.entity.card.CommentCard.ORDER_DEFAULT;
import static slasha.lanmu.entity.card.CommentCard.ORDER_TIME_REMOTEST_FIRST;

public class PostDetailActivity extends SameStyleActivity implements PostDetailContract.View {

    private static final String EXTRA_BOOK_POST_ID = "extra_book_post_id";
    private static final String TAG = "lanmu.detail";

    @BindView(R.id.recycler_view_comment)
    RecyclerView mRvComment;
    @BindView(R.id.recycler_view_post_images)
    RecyclerView mRvImage;
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
    @BindView(R.id.tv_order)
    TextView mTvOrder;
    @BindView(R.id.reply_board)
    ReplyBoard mReplyBoard;
    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;

    private LoadMoreWrapper<CommentCard> mLoadingMoreAdapter;
    private long mPostId;
    private int mCommentOrder = ORDER_DEFAULT;
    private int mPage = 1;
    private PostDetailContract.Presenter mPostDetailPresenter;

    public static Intent newIntent(Context context, long postId) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(EXTRA_BOOK_POST_ID, postId);
        return intent;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mPostId = bundle.getLong(EXTRA_BOOK_POST_ID, -1);
        if (mPostId == -1) {
            ToastUtils.showToast("Open post detail activity with -1 postId.");
            LogUtil.e(TAG, "Open post detail activity with -1 postId.");
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

        mScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    // Load More Data
                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        if (!mLoadingMoreAdapter.isFinishLoadMore()) {
                            doLoadMore();
                        }
                    }
                    mReplyBoard.close();
                });


        CommentAdapter commentAdapter = new CommentAdapter(this);
        commentAdapter.setCommentClickListener(new CommentClickListener());
        mLoadingMoreAdapter = new LoadMoreWrapper<>(commentAdapter,
                R.layout.layout_loading_footer,
                R.layout.layout_finished_footer);
        mRvComment.setAdapter(mLoadingMoreAdapter);
        mSwipeRefreshLayoutComments.setOnRefreshListener(() -> {
            mPage = 1;
            mLoadingMoreAdapter.setFinishedLoadMore(false);
            myPresenter().performPullPostDetail(mPostId);
        });
        mRvComment.setLayoutManager(new LinearLayoutManager(this));

        mReplyBoard.setPublisher(new Publisher() {
            @Override
            public void publishComment(CreateCommentModel model) {
                myPresenter().performPublishComment(model, new CommentLoadingProvider());
            }

            @Override
            public void publishCommentReply(CreateReplyModel model) {
                myPresenter().performPublishCommentReply(model, new CommentLoadingProvider());
            }
        });
    }

    private void doLoadMore() {
        myPresenter().performPullComments(mPostId, mCommentOrder, mPage, new CommentLoadingProvider());
    }

    @Override
    protected void initData() {
        myPresenter().performPullPostDetail(mPostId);
        mReplyBoard.setCommentModel(new CreateCommentModel(mPostId, UserInfo.id()));
    }

    @OnClick(R.id.tv_order)
    void switchOrder() {
        if (mCommentOrder == ORDER_DEFAULT) {
            mCommentOrder = ORDER_TIME_REMOTEST_FIRST;
            mTvOrder.setText(R.string.order_reverse);
        } else if (mCommentOrder == ORDER_TIME_REMOTEST_FIRST) {
            mCommentOrder = ORDER_COMMENT_THUMBS_UP_FIRST;
            mTvOrder.setText(R.string.order_comment_count);
        } else if (mCommentOrder == ORDER_COMMENT_THUMBS_UP_FIRST) {
            mCommentOrder = ORDER_DEFAULT;
            mTvOrder.setText(R.string.order_default);
        }
        mPage = 1;
        mLoadingMoreAdapter.setFinishedLoadMore(false);
        myPresenter().performPullComments(mPostId,
                mCommentOrder, mPage, new CommentLoadingProvider());
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_post_detail;
    }

    @Override
    public void showDetail(@NonNull List<BookPostCard> bookPosts) {
        BookPostCard bookPost = bookPosts.get(0);
        if (bookPost == null) {
            return;
        }
        setTitle(bookPost.getBook().getName());
        mTvPostContent.setText(bookPost.getContent());
        BookCard book = bookPost.getBook();
        if (book != null) {
            CommonUtils.setImage(mIvCover, book.getCoverUrl());
            mTvTitle.setText(book.getName());
            mTvCreatorName.setText(bookPost.getCreator().getName());
            mTvDescription.setText(String.format("%s/%s/%s",
                    book.getAuthor(),
                    book.getPublisher(),
                    FormatUtils.format2(book.getPublishDate())
            ));
            mCardView.setOnClickListener(l ->
                    AppUtils.jumpToBookInfoPage(PostDetailActivity.this, bookPost.getBook()));
        }

        UserCard creator = bookPost.getCreator();
        if (creator != null) {
            CommonUtils.setAvatar(mIvAvatar, creator.getAvatarUrl());
            mTvCreatorName.setText(creator.getName());
        }
        myPresenter().performPullComments(mPostId, mCommentOrder, mPage, this);
    }

    @Override
    public void showComments(PageModel<CommentCard> commentPage) {
        List<CommentCard> comments = commentPage.getResult();
        boolean end = commentPage.isEnd();
        int page = commentPage.getPage();
        int total = commentPage.getTotal();

        if (CommonUtils.isEmpty(comments)) {
            ToastUtils.showToast("一条评论也没有找到呀。。。");
            mLoadingMoreAdapter.setFinishedLoadMore(true);
            mLoadingMoreAdapter.notifyItemChanged(mLoadingMoreAdapter.getItemCount() - 1);
            mTvCommentCount.setText(String.format(String.format("%s%s",
                    getString(R.string.comments_title), getString(R.string.count)), 0));
        } else {
            mTvCommentCount.setText(String.format(String.format("%s%s",
                    getString(R.string.comments_title), getString(R.string.count)), total));
            if (end) {
                mLoadingMoreAdapter.setFinishedLoadMore(true);
            }
            if (page == 1) {
                mLoadingMoreAdapter.performDataSetChanged(comments);
            } else {
                mLoadingMoreAdapter.performDataSetAdded(comments);
            }
            mPage = page + 1;
        }
    }

    @Override
    public void showCreateCommentSuccess(CommentCard card) {
        ToastUtils.showToast(R.string.tip_create_comment_success);
        mReplyBoard.close();
        mReplyBoard.clear();
        mLoadingMoreAdapter.performSingleDataAdded(card);
    }

    @Override
    public void showCreateReplySuccess(CommentReplyCard card) {
        ToastUtils.showToast(R.string.tip_create_reply_success);
        int pos = (int) mReplyBoard.getTag();
        if (pos < mLoadingMoreAdapter.getEntities().size()) {
            CommentCard commentCard = mLoadingMoreAdapter.getEntities().get(pos);
            commentCard.getReplies().add(card);
            mLoadingMoreAdapter.notifyItemChanged(pos);
        }
        mReplyBoard.close();
        mReplyBoard.clear();
    }

    @Override
    public PostDetailContract.Presenter myPresenter() {
        if (mPostDetailPresenter == null) {
            mPostDetailPresenter = new PostDetailPresenterImpl(this);
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

    private class CommentLoadingProvider implements LoadingProvider {

        @Override
        public void showLoadingIndicator() {
            showProgressDialog();
        }

        @Override
        public void hideLoadingIndicator() {
            hideProgressDialog();
        }
    }

    private class CommentClickListener implements CommentAdapter.CommentClickListener {
        @Override
        public void onContentClick(CommentCard comment, int position) {
            CreateReplyModel createReplyModel = new CreateReplyModel();
            createReplyModel.setCommentId(comment.getId());
            createReplyModel.setFromId(UserInfo.id());
            createReplyModel.setFromName(UserInfo.self().getName());
            createReplyModel.setCommentOwnerName(comment.getFrom().getName());
            createReplyModel.setToId(-1);
            createReplyModel.setToName(null);
            mReplyBoard.openAndAttach(createReplyModel, position);
        }

        @Override
        public void onReplyItemClick(CommentReplyCard reply, int position) {
            CreateReplyModel createReplyModel = new CreateReplyModel();
            createReplyModel.setCommentId(reply.getCommentId());
            createReplyModel.setFromId(UserInfo.id());
            createReplyModel.setFromName(UserInfo.self().getName());
            createReplyModel.setCommentOwnerName(null);
            createReplyModel.setToId(reply.getFromId());
            createReplyModel.setToName(reply.getFromName());
            mReplyBoard.openAndAttach(createReplyModel, position);
        }

        @Override
        public void onReplyExpandedItemClick(long commentId, int position) {
            AppUtils.jumpToReplyPage(PostDetailActivity.this, commentId);
        }


        @Override
        public void onAvatarClick(UserCard user) {
            AppUtils.jumpToUserProfile(PostDetailActivity.this, user.getId());
        }

        @Override
        public void onThumbsUpClick(CommentCard comment) {
            // send a request to insert a record of thumbs up
            myPresenter().performThumbsUp(UserInfo.id(), comment.getId());
        }
    }


}
