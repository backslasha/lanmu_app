package slasha.lanmu.business.main.flow;

import java.util.List;

import androidx.annotation.MainThread;
import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.card.BookPostCard;

public interface FlowContract {

    interface View extends BaseView<Presenter> {
        @MainThread
        void showBookPosts(List<BookPostCard> bookPostCards);
    }

    interface Presenter extends BasePresenter {

        void performPullBookPosts(FlowFragment.FlowType flowType);
    }

}
