package slasha.lanmu.business.main;

import slasha.lanmu.BaseModel;
import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.bean.BookPost;
import slasha.lanmu.bean.BookPostFlow;

public interface MainContract {

    interface MainView extends BaseView<MainPresenter> {
        void showBookPostFlow(BookPostFlow bookPostFlow);
    }

    interface MainPresenter extends BasePresenter {
        void performPullBookPostFlow();
    }

    interface MainModel extends BaseModel {
        BookPostFlow offerPostFlow();
    }
}
