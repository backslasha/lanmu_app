package slasha.lanmu.business.post_detail.reply;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import slasha.lanmu.R;
import slasha.lanmu.business.post_detail.apdater.CommentReplyAdapter;
import slasha.lanmu.entity.api.base.PageModel;
import slasha.lanmu.entity.card.CommentReplyCard;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.common.LogUtil;
import slasha.lanmu.utils.common.ToastUtils;
import yhb.chorus.common.adapter.wrapper.LoadMoreWrapper;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class ReplyFragment extends BottomSheetDialogFragment
        implements ReplyContract.View {


    private static final String ARG_COMMENT_ID = "arg_comment_id ";
    private long mCommentId;
    private RecyclerView mRecyclerView;
    private TextView mTvTitle;
    private int mPage = 1;
    private LoadMoreWrapper<CommentReplyCard> mLoadMoreAdapter;
    private ReplyContract.Presenter mPresenter;
    private Context mContext;

    public ReplyFragment() {
        super();
    }

    public static ReplyFragment newInstance(long commentId) {
        Bundle args = new Bundle();
        ReplyFragment fragment = new ReplyFragment();
        args.putLong(ARG_COMMENT_ID, commentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (getArguments() != null) {
            mCommentId = getArguments().getLong(ARG_COMMENT_ID, -1);
            if (mCommentId == -1) {
                LogUtil.e(TAG, "commentId == -1!");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_replies, new FrameLayout(mContext), false);
        mRecyclerView = inflate.findViewById(R.id.recycler_view);
        View ivClose = inflate.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(v -> dismiss());
        mTvTitle = inflate.findViewById(R.id.tv_title);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = MATCH_PARENT;
            lp.height = MATCH_PARENT;
            window.setAttributes(lp);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CommentReplyAdapter replyAdapter = new CommentReplyAdapter(view.getContext());
        mLoadMoreAdapter = new LoadMoreWrapper<>(replyAdapter, R.layout.layout_loading_footer,
                R.layout.layout_finished_footer);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mLoadMoreAdapter);
        mRecyclerView.addOnScrollListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            protected void onLoading(int countItem, int lastItem) {
                myPresenter().performPullReplies(mCommentId, mPage);
            }
        });
        myPresenter().performPullReplies(mCommentId, mPage);
    }

    @Override
    public void showPullReliesSuccess(PageModel<CommentReplyCard> model) {
        final int page = model.getPage();
        final int total = model.getTotal();
        mTvTitle.setText(String.format(Locale.CHINA, "全部共%d条回复", total));
        List<CommentReplyCard> result = model.getResult();
        if (CommonUtils.isEmpty(result)) {
            ToastUtils.showToast("一条回复都没有啊。。。");
            mLoadMoreAdapter.setFinishedLoadMore(true);
            mLoadMoreAdapter.notifyItemChanged(mLoadMoreAdapter.getItemCount() - 1);
        } else {
            if (model.isEnd()) {
                mLoadMoreAdapter.setFinishedLoadMore(true);
            } else {
                mPage = page + 1;
            }
            mLoadMoreAdapter.performDataSetAdded(result);
        }
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

    }

    @Override
    public void hideLoadingIndicator() {

    }
}
