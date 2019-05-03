package slasha.lanmu.business.notification;

import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.api.comment.NotifyRspModel;

public interface NotificationContract {

    interface View extends BaseView<Presenter> {

        void showPullNotificationsSuccess(NotifyRspModel notifyRspModel);
    }

    interface Presenter extends BasePresenter {

        void performPullNotifications(long userId);
    }

}
