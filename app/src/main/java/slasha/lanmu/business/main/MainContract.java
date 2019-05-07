package slasha.lanmu.business.main;

import java.util.List;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import slasha.lanmu.BaseModel;
import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.entity.response.BookPostFlow;

public interface MainContract {

    interface MainView extends BaseView<MainPresenter> {
        @MainThread
        void showBookPosts(List<BookPostCard> bookPostCards);
    }

    interface MainPresenter extends BasePresenter {
        void performPullBookPosts(BookPostFlowFragment.FlowType flowType);
    }

}
