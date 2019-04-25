package slasha.lanmu.business.create_post;

import java.util.List;

import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.local.BookPost;

public interface CreatePostContract {

    interface CreatePostView extends BaseView<CreatePostPresenter> {
        void showBookPosts(List<BookPost> bookPosts);
    }

    interface CreatePostPresenter extends BasePresenter {
        void performQuery(String keyword);
    }

}
