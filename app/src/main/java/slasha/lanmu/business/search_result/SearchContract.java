package slasha.lanmu.business.search_result;

import java.util.List;

import slasha.lanmu.BaseModel;
import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.bean.BookPost;

public interface SearchContract {

    interface SearchView extends BaseView<SearchPresenter> {
        void showBookPosts(List<BookPost> bookPosts);
    }

    interface SearchPresenter extends BasePresenter {
        void performQuery(String keyword);
    }

    interface SearchModel extends BaseModel<BookPost>{

    }
}
