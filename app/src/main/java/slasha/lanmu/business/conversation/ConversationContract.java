package slasha.lanmu.business.conversation;

import java.util.List;

import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.card.MessageCard;

public interface ConversationContract {
    interface View extends BaseView<Presenter> {
        void showPullConversationSuccess(List<MessageCard> cards);
    }

    interface Presenter extends BasePresenter {
        void performPullConversations(long userId);
    }
}
