package slasha.lanmu.business.chat;

import java.util.List;

import slasha.lanmu.BaseModel;
import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.api.message.CreateMsgModel;
import slasha.lanmu.entity.api.message.PullMsgModel;
import slasha.lanmu.entity.card.MessageCard;
import slasha.lanmu.entity.local.Message;

public interface ChatContract {

    interface View extends BaseView<Presenter> {
        void showMessages(List<MessageCard> messages);

        void showSendMsgSuccess(MessageCard card);

        void showSendMsgFail(String info);
    }

    interface Presenter extends BasePresenter {
        void performPullMessages(PullMsgModel msgModel);

        void performPullMessagesLocally(long talk2Id);

        void performSendMessage(CreateMsgModel model);
    }
}
