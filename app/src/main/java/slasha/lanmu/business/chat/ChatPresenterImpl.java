package slasha.lanmu.business.chat;

import androidx.annotation.NonNull;

import java.util.List;

import slasha.lanmu.GlobalBuffer;
import slasha.lanmu.bean.Message;
import slasha.lanmu.debug.ArtificialProductFactory;

class ChatPresenterImpl implements ChatContract.ChatPresenter {

    private final ChatContract.ChatModel mChatModel;
    private final ChatContract.ChatView mChatView;

    ChatPresenterImpl(@NonNull ChatContract.ChatModel chatModel,
                      @NonNull ChatContract.ChatView chatView) {
        mChatModel = chatModel;
        mChatView = chatView;
    }

    @Override
    public void performPullMessages(long myId, long otherId) {
        List<Message> result;
        if (GlobalBuffer.Debug.sUseFakeData) {
            result = ArtificialProductFactory.messages();
        } else {
            result = mChatModel.offer(myId, otherId);
        }
        mChatView.showMessages(result);
    }
}
