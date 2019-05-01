package slasha.lanmu.business.message.conversation;

import java.util.List;

import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.card.ConversationCard;

public interface ConversationContract {
    interface View extends BaseView<Presenter> {
        void showPullConversationSuccess(List<ConversationCard> cards);
    }

    interface Presenter extends BasePresenter {
        void performPullConversations(long userId);
    }
}
