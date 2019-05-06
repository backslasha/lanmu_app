package slasha.lanmu.business.friend.apply;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import slasha.lanmu.LoadingProvider;
import slasha.lanmu.entity.api.base.RspModelWrapper;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.net.Network;
import slasha.lanmu.utils.PresenterHelper;
import slasha.lanmu.utils.common.LogUtil;

class ApplyPresenterImpl implements ApplyContract.Presenter {

    private static final String TAG = "lanmu.apply";

    private ApplyContract.View mView;

    ApplyPresenterImpl(ApplyContract.View view) {
        mView = view;
    }

    @Override
    public void performPullApplies(long userId) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote()::pullApplies,
                userId,
                mView::showPullAppliesSuccess,
                mView::showActionFail,
                mView
        );
    }

    @Override
    public void performAddFriend(long fromId, long toId, LoadingProvider loadingProvider) {
        Call<RspModelWrapper<UserCard>> rspModelWrapperCall
                = Network.remote().doAddFriend(toId, fromId);
        loadingProvider.showLoadingIndicator();
        rspModelWrapperCall.enqueue(new Callback<RspModelWrapper<UserCard>>() {
            @Override
            public void onResponse(Call<RspModelWrapper<UserCard>> call,
                                   Response<RspModelWrapper<UserCard>> response) {
                LogUtil.i(TAG, response.body().toString());
                mView.showAddFriendSuccess(response.body().getResult());
                loadingProvider.hideLoadingIndicator();
            }

            @Override
            public void onFailure(Call<RspModelWrapper<UserCard>> call, Throwable t) {
                LogUtil.i(TAG, String.valueOf(t.getCause()));
                mView.showActionFail(String.valueOf(t.getCause()));
                loadingProvider.hideLoadingIndicator();
            }
        });
    }

    @Override
    public void performRejectApply(long applyId, LoadingProvider loadingProvider) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote()::doRejectApply,
                applyId,
                mView::showRejectApplySuccess,
                mView::showActionFail,
                loadingProvider
        );
    }
}
