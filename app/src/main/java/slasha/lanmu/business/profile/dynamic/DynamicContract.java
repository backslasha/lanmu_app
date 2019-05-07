package slasha.lanmu.business.profile.dynamic;

import java.util.List;

import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.card.DynamicCard;

public interface DynamicContract {
    interface View extends BaseView<Presenter> {
        void showPullThumbsUpsSuccess(List<DynamicCard> cards);

        void showPullCommentsSuccess(List<DynamicCard> cards);

        void showPullPostsSuccess(List<DynamicCard> cards);

        void showPullRepliesSuccess(List<DynamicCard> cards);
    }

    interface Presenter extends BasePresenter {
        void performPullThumbsUps(long userId);

        void performPullComments(long userId);

        void performPullReplies(long userId);

        void performPullPosts(long userId);

        void performPullDynamicByType(long userId, int type);
    }
}
