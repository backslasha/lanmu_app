package slasha.lanmu.business.notification;

import slasha.lanmu.net.Network;
import slasha.lanmu.utils.PresenterHelper;

public class NotificationPresenterImpl implements NotificationContract.Presenter {

    private static final String TAG = "lanmu.notification";

    NotificationPresenterImpl(NotificationContract.View view) {
        mView = view;
    }

    private NotificationContract.View mView;

    @Override
    public void performPullNotifications(long userId) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote()::pullNotifications,
                userId,
                mView::showPullNotificationsSuccess,
                mView::showActionFail,
                mView
        );
    }
}
