package slasha.lanmu.business.chat;

import java.util.List;

import slasha.lanmu.BaseModel;
import slasha.lanmu.BasePresenter;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.local.Message;

public interface ChatContract {

    interface ChatView extends BaseView<ChatPresenter> {
        void showMessages(List<Message> messages);
    }

    interface ChatPresenter extends BasePresenter {
        void performPullMessages(long myId, long otherId);
    }

    interface ChatModel extends BaseModel {
        List<Message> offer(long myId, long otherId);
    }
}
