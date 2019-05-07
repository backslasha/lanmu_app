package slasha.lanmu.business.profile.dynamic;

import android.os.Bundle;
import android.view.View;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import slasha.lanmu.BaseFragment;
import slasha.lanmu.R;
import slasha.lanmu.business.profile.ProfileDynamicAdapter;
import slasha.lanmu.entity.card.DynamicCard;
import slasha.lanmu.utils.common.ToastUtils;

public class DynamicFragment extends BaseFragment
        implements DynamicContract.View {

    private static final String ARG_USER_ID = "arg_user_id";
    private static final String ARG_DYNAMIC_TYPE = "arg_dynamic_type";

    public static DynamicFragment newInstance(long userId, int type) {
        Bundle args = new Bundle();
        args.putLong(ARG_USER_ID, userId);
        args.putInt(ARG_DYNAMIC_TYPE, type);
        DynamicFragment fragment = new DynamicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private DynamicContract.Presenter mPresenter;
    private ProfileDynamicAdapter mAdapter;
    private long mUserId;
    private int mType;
    private boolean mInitiated;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_refresh_recycler_view;
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mUserId = bundle.getLong(ARG_USER_ID);
        mType = bundle.getInt(ARG_DYNAMIC_TYPE);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new ProfileDynamicAdapter(getContext(), mUserId));
        mSwipeRefreshLayout.setOnRefreshListener(()
                -> myPresenter().performPullDynamicByType(mUserId, mType));
    }

    @Override
    protected void initData() {
        super.initData();
        if (!mInitiated) {
            myPresenter().performPullDynamicByType(mUserId, mType);
            mInitiated = true;
        }
    }


    @Override
    public DynamicContract.Presenter myPresenter() {
        if (mPresenter == null) {
            mPresenter = new DynamicPresenterImpl(this);
        }
        return mPresenter;
    }

    @Override
    public void showActionFail(String message) {
        ToastUtils.showToast(getString(R.string.tip_info_load_failed) + message);
    }

    @Override
    public void showLoadingIndicator() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingIndicator() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showPullThumbsUpsSuccess(List<DynamicCard> cards) {
        mAdapter.performDataSetChanged(cards);
    }

    @Override
    public void showPullCommentsSuccess(List<DynamicCard> cards) {
        mAdapter.performDataSetChanged(cards);
    }

    @Override
    public void showPullPostsSuccess(List<DynamicCard> cards) {
        mAdapter.performDataSetChanged(cards);
    }

    @Override
    public void showPullRepliesSuccess(List<DynamicCard> cards) {
        mAdapter.performDataSetChanged(cards);
    }


}
