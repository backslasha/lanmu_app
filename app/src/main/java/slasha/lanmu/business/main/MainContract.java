package slasha.lanmu.business.main;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import slasha.lanmu.BaseModel;
import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.bean.BookPostFlow;

public interface MainContract {

    interface MainView extends BaseView<MainPresenter> {
        @MainThread
        void showBookPostFlow(BookPostFlow bookPostFlow);
    }

    interface MainPresenter extends BasePresenter {
        void performPullBookPostFlow(BookPostFlowFragment.FlowType flowType);
    }

    interface MainModel extends BaseModel {
        @WorkerThread
        void offerPostFlow(int type, Callback<BookPostFlow> postFlowCallback);
    }
}
