package slasha.lanmu.business.conversation;

import slasha.lanmu.net.Network;
import slasha.lanmu.utils.PresenterHelper;

class ConversationPresenterImpl implements ConversationContract.Presenter{

    private static final String TAG = "lanmu.message";

    private ConversationContract.View mView;

    ConversationPresenterImpl(ConversationContract.View view) {
        mView = view;
    }

    @Override
    public void performPullConversations(long userId) {
        PresenterHelper.requestAndHandleResponse(
                TAG,
                Network.remote()::pullConversations,
                userId,
                mView::showPullConversationSuccess,
                mView::showActionFail,
                mView
        );

    }
}
