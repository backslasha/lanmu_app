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

import static slasha.lanmu.entity.card.CommentCard.ORDER_COMMENT_THUMBS_UP_FIRST;
import static slasha.lanmu.entity.card.CommentCard.ORDER_DEFAULT;
import static slasha.lanmu.entity.card.CommentCard.ORDER_TIME_REMOTEST_FIRST;

public class PostDetailActivity extends SameStyleActivity
        implements PostDetailContract.View {

    private static final String EXTRA_BOOK_POST_ID = "extra_book_post_id";
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
    @BindView(R.id.tv_order)
    TextView mTvOrder;
    @BindView(R.id.reply_board)
    ReplyBoard mReplyBoard;
    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;

    private CommentAdapter mCommentAdapter;
    private long mPostId;
    private int mCommentOrder = ORDER_DEFAULT;
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

        mScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (
                v, scrollX, scrollY, oldScrollX, oldScrollY) -> mReplyBoard.close());

        mSwipeRefreshLayoutComments.setOnRefreshListener(() ->
                myPresenter().performPullComments(mPostId, mCommentOrder, PostDetailActivity.this));

        mRecyclerViewComments.setLayoutManager(new LinearLayoutManager(this));

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
        myPresenter().performPullComments(mPostId, mCommentOrder, new CommentLoadingProvider());
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
        myPresenter().performPullComments(mPostId, mCommentOrder, this);
        showLoadingIndicator();
    }

    @Override
    public void showComments(List<CommentCard> comments) {
        if (CommonUtils.isEmpty(comments)) {
            ToastUtils.showToast("no comments found!");
        } else {
            if (mCommentAdapter == null) {
                mCommentAdapter = new CommentAdapter(this);
                mCommentAdapter.setCommentClickListener(new CommentClickListener());
                mRecyclerViewComments.setAdapter(mCommentAdapter);
            }
            mCommentAdapter.performDataSetChanged(comments);
            int count = comments.size();
            if (count > 0) {
                mTvCommentCount.setText(String.format(String.format("%s%s",
                        getString(R.string.comments_title),
                        getString(R.string.count)), count));
            }
        }
    }

    @Override
    public void showCreateCommentSuccess(CommentCard card) {
        ToastUtils.showToast(R.string.tip_create_comment_success);
        mReplyBoard.close();
        mReplyBoard.clear();
        mCommentAdapter.performSingleDataAdded(card);
    }

    @Override
    public void showCreateReplySuccess(CommentReplyCard card) {
        ToastUtils.showToast(R.string.tip_create_reply_success);
        int pos = (int) mReplyBoard.getTag();
        if (pos < mCommentAdapter.getEntities().size()) {
            CommentCard commentCard = mCommentAdapter.getEntities().get(pos);
            commentCard.getReplies().add(card);
            mCommentAdapter.notifyItemChanged(pos);
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
        public void onCommentReplyClick(boolean isExpandableItem,
                                        CommentReplyCard reply, int position) {
            if (isExpandableItem) {
                ToastUtils.showToast("jump to reply list!");
            } else {
                CreateReplyModel createReplyModel = new CreateReplyModel();
                createReplyModel.setCommentId(reply.getCommentId());
                createReplyModel.setFromId(UserInfo.id());
                createReplyModel.setFromName(UserInfo.self().getName());
                createReplyModel.setCommentOwnerName(null);
                createReplyModel.setToId(reply.getFromId());
                createReplyModel.setToName(reply.getFromName());
                mReplyBoard.openAndAttach(createReplyModel, position);
            }
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
