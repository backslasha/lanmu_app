package slasha.lanmu.business.create_post;

import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.api.post.CreatePostModel;
import slasha.lanmu.entity.card.BookPostCard;

public interface CreatePostContract {

    interface View extends BaseView<Presenter> {
        void showCreateSuccess(BookPostCard card);

        void showCreateFail(String info);
    }

    interface Presenter extends BasePresenter {
        void performCreate(CreatePostModel model);
    }

}
