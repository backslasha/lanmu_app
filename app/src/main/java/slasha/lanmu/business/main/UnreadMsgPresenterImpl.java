package slasha.lanmu.business.main;

import slasha.lanmu.net.Network;
import slasha.lanmu.utils.PresenterHelper;

class UnreadMsgPresenterImpl implements UnreadMsgContract.Presenter {

    private static final String TAG = "lanmu.unread";
    private UnreadMsgContract.View mView;

    UnreadMsgPresenterImpl(UnreadMsgContract.View view) {
        mView = view;
    }

    @Override
    public void performQueryGlobalNotifyCount(long userId) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote()::globalNotifyCount,
                userId,
                mView::showPullGlobalNotifyCountSuccess,
                mView::showActionFail,
                mView
        );
    }
}
