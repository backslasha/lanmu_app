package slasha.lanmu.business.search_result;

import java.util.List;

import slasha.lanmu.BaseModel;
import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.entity.local.BookPost;

public interface SearchContract {

    interface View extends BaseView<SearchPresenter> {
        void showBookPosts(List<BookPostCard> bookPosts);
    }

    interface SearchPresenter extends BasePresenter {
        void performQuery(String keyword);
    }

    interface SearchModel extends BaseModel {
        List<BookPost> offer(String keyword);
    }
}
