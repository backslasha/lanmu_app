package slasha.lanmu.business.profile;

import slasha.lanmu.net.Network;
import slasha.lanmu.utils.PresenterHelper;

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
                mView,
                Network.remote()::pullDynamics,
                userId,
                mView::showPullDynamicsSuccess
        );
    }
}
