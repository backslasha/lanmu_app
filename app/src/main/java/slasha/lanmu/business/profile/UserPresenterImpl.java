package slasha.lanmu.business.profile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import slasha.lanmu.entity.api.base.RspModelWrapper;
import slasha.lanmu.net.Network;
import slasha.lanmu.utils.PresenterHelper;
import slasha.lanmu.utils.common.LogUtil;

public class UserPresenterImpl implements ProfileContract.Presenter {

    private ProfileContract.View mView;
    private static final String TAG = "lanmu.profile.dynamic";

    UserPresenterImpl(ProfileContract.View view) {
        mView = view;
    }

    @Override
    public void performPullDynamics(long userId) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote()::pullDynamics,
                userId,
                mView::showPullDynamicsSuccess,
                mView::showActionFail,
                mView
        );
    }

    @Override
    public void performPullProfile(long userId) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote()::searchProfile,
                userId,
                mView::showProfile,
                mView::showActionFail,
                mView
        );
    }

    @Override
    public void performSendFriendApply(long fromId, long toId) {

        Call<RspModelWrapper> rspModelWrapperCall
                = Network.remote().doApply(toId, fromId);
        mView.showLoadingIndicator();
        rspModelWrapperCall.enqueue(new Callback<RspModelWrapper>() {
            @Override
            public void onResponse(Call<RspModelWrapper> call,
                                   Response<RspModelWrapper> response) {
                LogUtil.i(TAG, response.body().toString());
                mView.showSendApplySuccess();
                mView.hideLoadingIndicator();
            }

            @Override
            public void onFailure(Call<RspModelWrapper> call, Throwable t) {
                LogUtil.i(TAG, String.valueOf(t.getCause()));
                mView.showActionFail(String.valueOf(t.getCause()));
                mView.hideLoadingIndicator();
            }
        });

    }
}
