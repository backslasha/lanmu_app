package slasha.lanmu.business.post_detail.reply;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.business.post_detail.apdater.CommentReplyAdapter;
import slasha.lanmu.entity.api.base.PageModel;
import slasha.lanmu.entity.api.comment.CreateCommentModel;
import slasha.lanmu.entity.api.comment.CreateReplyModel;
import slasha.lanmu.entity.card.CommentReplyCard;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.common.LogUtil;
import slasha.lanmu.utils.common.ToastUtils;
import slasha.lanmu.widget.reply.Publisher;
import slasha.lanmu.widget.reply.ReplyBoard;
import yhb.chorus.common.adapter.wrapper.LoadMoreWrapper;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class ReplyActivity extends SameStyleActivity implements ReplyContract.View {

    private static final String EXTRA_COMMENT_ID = "arg_comment_id ";

    private long mCommentId;
    private int mPage = 1;
    private LoadMoreWrapper<CommentReplyCard> mLoadMoreAdapter;
    private ReplyContract.Presenter mPresenter;
    private LinearLayoutManager mLinearLayoutManager;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.reply_board)
    ReplyBoard mReplyBoard;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static Intent newIntent(Context context, long commentId) {
        Intent intent = new Intent(context, ReplyActivity.class);
        intent.putExtra(EXTRA_COMMENT_ID, commentId);
        return intent;
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_reply;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mCommentId = bundle.getLong(EXTRA_COMMENT_ID, -1);
        if (mCommentId == -1) {
            LogUtil.e(TAG, "commentId == -1!");
        }
        return true;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mToolbar.setBackgroundColor(Color.WHITE);
        mToolbar.setTitleTextColor(Color.BLACK);
        mToolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        mReplyBoard.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        CommentReplyAdapter replyAdapter = new CommentReplyAdapter(this);
        replyAdapter.setOnItemClickListener(new CommentReplyAdapter.onItemClickListener() {
            @Override
            public void onItemClick(CommentReplyCard reply) {
                CreateReplyModel createReplyModel = new CreateReplyModel();
                createReplyModel.setCommentId(reply.getCommentId());
                createReplyModel.setFromId(UserInfo.id());
                createReplyModel.setFromName(UserInfo.self().getName());
                createReplyModel.setCommentOwnerName(null);
                createReplyModel.setToId(reply.getFromId());
                createReplyModel.setToName(reply.getFromName());
                mReplyBoard.openAndAttach(createReplyModel, 0);
                mReplyBoard.setVisibility(View.VISIBLE);
            }

            @Override
            public void onExpandedItemClick(long commentId) {

            }
        });

        mLoadMoreAdapter = new LoadMoreWrapper<>(replyAdapter,
                R.layout.layout_loading_footer,
                R.layout.layout_finished_footer);
        mRecyclerView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mLoadMoreAdapter);
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRecyclerView.addOnScrollListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            protected void onLoading(int countItem, int lastItem) {
                myPresenter().performPullReplies(mCommentId, mPage);
            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                mReplyBoard.close();
                mReplyBoard.setVisibility(View.GONE);
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        mReplyBoard.setPublisher(new Publisher() {
            @Override
            public void publishComment(CreateCommentModel model) {

            }

            @Override
            public void publishCommentReply(CreateReplyModel model) {
                myPresenter().performPublishCommentReply(model);
            }
        });
        myPresenter().performPullReplies(mCommentId, mPage);
    }


    @Override
    public void showPullReliesSuccess(PageModel<CommentReplyCard> model) {
        final int page = model.getPage();
        final int total = model.getTotal();
        mToolbar.setTitle(String.format(Locale.CHINA, "全部共%d条回复", total));
        List<CommentReplyCard> result = model.getResult();
        if (CommonUtils.isEmpty(result)) {
            ToastUtils.showToast("一条回复都没有啊。。。");
            mLoadMoreAdapter.setFinishedLoadMore(true);
            mLoadMoreAdapter.notifyItemChanged(mLoadMoreAdapter.getItemCount() - 1);
        } else {
            if (model.isEnd()) {
                mLoadMoreAdapter.setFinishedLoadMore(true);
                mLoadMoreAdapter.performDataSetAdded(result);
            } else {
                mPage += 1;
                mRecyclerView.getViewTreeObserver()
                        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                mRecyclerView.getViewTreeObserver()
                                        .removeOnGlobalLayoutListener(this);

                                // 一页如果没有填充满整个页面
                                if (mLinearLayoutManager.findLastVisibleItemPosition()
                                        == mLoadMoreAdapter.getItemCount() - 1) {
                                    ReplyActivity.this.myPresenter()
                                            .performPullReplies(mCommentId, mPage);
                                }
                            }
                        });
                mLoadMoreAdapter.performDataSetAdded(result);

            }

        }
    }

    @Override
    public void showCreateReplySuccess(CommentReplyCard card) {
        ToastUtils.showToast(R.string.tip_create_reply_success);
        mLoadMoreAdapter.performSingleDataAdded(card);
        mReplyBoard.setVisibility(View.GONE);
        mReplyBoard.close();
        mReplyBoard.clear();
    }

    @Override
    public ReplyContract.Presenter myPresenter() {
        if (mPresenter == null) {
            mPresenter = new ReplyPresenterImpl(this);
        }
        return mPresenter;
    }

    @Override
    public void showActionFail(String message) {
        ToastUtils.showToast("拉取回复失败：" + message);
    }

    @Override
    public void showLoadingIndicator() {
        showProgressDialog();
    }

    @Override
    public void hideLoadingIndicator() {
        hideProgressDialog();
    }
}

