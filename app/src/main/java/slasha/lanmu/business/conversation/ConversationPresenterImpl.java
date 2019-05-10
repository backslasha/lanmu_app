package slasha.lanmu.business.conversation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import slasha.lanmu.entity.api.base.RspModelWrapper;
import slasha.lanmu.entity.api.base.UnreadModel;
import slasha.lanmu.entity.card.MessageCard;
import slasha.lanmu.entity.card.UnreadMessagesCard;
import slasha.lanmu.net.Network;
import slasha.lanmu.persistence.db.LanmuDB;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.PresenterHelper;
import slasha.lanmu.utils.common.LogUtil;
import slasha.lanmu.utils.common.ThreadUtils;

class ConversationPresenterImpl implements ConversationContract.Presenter {

    private static final String TAG = "lanmu.message";

    private ConversationContract.View mView;

    ConversationPresenterImpl(ConversationContract.View view) {
        mView = view;
    }

    @Override
    public void performPullConversations(long userId, boolean syncServer) {
        mView.showLoadingIndicator();
        ThreadUtils.execute(() -> {
            List<UnreadModel<MessageCard>> messageCards = LanmuDB.queryConversations();
            AppUtils.runOnUiThread(() -> {
                if (syncServer) {
                    performPullUnreadMessages(userId);
                } else {
                    mView.hideLoadingIndicator();
                }
                mView.showPullConversationLocallySuccess(messageCards);
            });
        });
    }

    @Override
    public void performPullUnreadMessages(long userId) {
        Call<RspModelWrapper<List<UnreadMessagesCard>>> rspModelWrapperCall
                = Network.remote().pullUnreadMessages(userId);
        mView.showLoadingIndicator();
        rspModelWrapperCall.enqueue(new Callback<RspModelWrapper<List<UnreadMessagesCard>>>() {
            @Override
            public void onResponse(Call<RspModelWrapper<List<UnreadMessagesCard>>> call,
                                   Response<RspModelWrapper<List<UnreadMessagesCard>>> response) {
                LogUtil.i(TAG, "onResponse -> " + response.raw().toString());
                RspModelWrapper<List<UnreadMessagesCard>> rspModel = response.body();
                if (rspModel != null && rspModel.success()) {
                    List<UnreadMessagesCard> result = rspModel.getResult();
                    LogUtil.i(TAG, "result -> " + result.toString());
                    cacheUnreadMessages(result); // 将未读消息插入本地数据库
                    AppUtils.runOnUiThread(() -> {
                        mView.showPullUnreadMessagesSuccess(result);
                        mView.hideLoadingIndicator();
                    });
                } else {
                    AppUtils.runOnUiThread(() -> {
                        mView.showActionFail(
                                rspModel == null ? "empty response!" : rspModel.getMessage());
                        mView.hideLoadingIndicator();
                    });
                }
            }

            @Override
            public void onFailure(Call<RspModelWrapper<List<UnreadMessagesCard>>> call,
                                  Throwable t) {
                PresenterHelper.handleFailAction(TAG, t, mView::showActionFail, mView);
            }
        });
    }

    private void cacheUnreadMessages(List<UnreadMessagesCard> result) {
        List<MessageCard> unread = new ArrayList<>();
        for (UnreadMessagesCard card : result) {
            unread.addAll(card.getUnreadMsgCards());
        }
        MessageCard[] messages = unread.toArray(new MessageCard[0]);
        if (messages != null) {
            LanmuDB.saveMessages(messages);
        }
    }
}
