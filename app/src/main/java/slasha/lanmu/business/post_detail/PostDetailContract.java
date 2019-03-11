package slasha.lanmu.business.post_detail;

import slasha.lanmu.BaseModel;
import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.bean.BookPost;

public interface PostDetailContract {

    interface PostDetailView extends BaseView<PostDetailPresenter> {
        void showDetail(BookPost bookPost);
    }

    interface PostDetailPresenter extends BasePresenter {

    }

    interface PostDetailModel extends BaseModel<BookPost> {

    }
}
