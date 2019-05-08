package slasha.lanmu.business.main;

import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.api.notify.GlobalNotifyRspModel;

public interface UnreadMsgContract {

    interface View extends BaseView<Presenter> {
        void showPullGlobalNotifyCountSuccess(GlobalNotifyRspModel model);
    }

    interface Presenter extends BasePresenter {
        void performQueryGlobalNotifyCount(long userId);
    }

}
