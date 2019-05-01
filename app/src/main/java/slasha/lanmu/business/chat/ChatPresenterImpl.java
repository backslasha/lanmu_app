package slasha.lanmu.business.chat;

import androidx.annotation.NonNull;
import slasha.lanmu.entity.api.message.CreateMsgModel;
import slasha.lanmu.entity.api.message.PullMsgModel;
import slasha.lanmu.net.Network;
import slasha.lanmu.utils.PresenterHelper;

class ChatPresenterImpl implements ChatContract.Presenter {

    private static final String TAG = "lanmu.message";

    private final ChatContract.View mView;

    ChatPresenterImpl(@NonNull ChatContract.View view) {
        mView = view;
    }

    @Override
    public void performPullMessages(PullMsgModel model) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote()::pullMsgRecords,
                model,
                mView::showMessages,
                mView::showActionFail,
                mView
        );
    }

    @Override
    public void performSendMessage(CreateMsgModel model) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote()::createMsg,
                model,
                mView::showSendMsgSuccess,
                mView::showSendMsgFail,
                mView
        );
    }
}
