package slasha.lanmu.business.chat;

import java.util.List;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import slasha.lanmu.entity.api.base.RspModelWrapper;
import slasha.lanmu.entity.api.message.CreateMsgModel;
import slasha.lanmu.entity.api.message.PullMsgModel;
import slasha.lanmu.entity.card.MessageCard;
import slasha.lanmu.net.Network;
import slasha.lanmu.persistence.db.LanmuDB;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.PresenterHelper;
import slasha.lanmu.utils.common.LogUtil;
import slasha.lanmu.utils.common.ThreadUtils;

class ChatPresenterImpl implements ChatContract.Presenter {

    private static final String TAG = "lanmu.message";

    private final ChatContract.View mView;

    ChatPresenterImpl(@NonNull ChatContract.View view) {
        mView = view;
    }

    @Override
    public void performPullMessages(PullMsgModel model) {
//        PresenterHelper.requestAndHandleResponse(
//                TAG,
//                Network.remote()::pullMsgRecords,
//                model,
//                mView::showMessages,
//                mView::showActionFail,
//                mView
//        );
    }

    @Override
    public void performPullMessagesLocally(long talk2Id) {
        mView.showLoadingIndicator();
        ThreadUtils.execute(() -> {
            List<MessageCard> cards = LanmuDB.queryMessages(talk2Id);
            LanmuDB.markMsgReceived(talk2Id); // 更新本地数据库该 talk2Id 所有私信为已读
            AppUtils.runOnUiThread(() -> {
                mView.showMessages(cards);
                mView.hideLoadingIndicator();
            });
        });
    }

    @Override
    public void performSendMessage(CreateMsgModel model) {
        Call<RspModelWrapper<MessageCard>> msg
                = Network.remote().createMsg(model);
        mView.showLoadingIndicator();
        msg.enqueue(new Callback<RspModelWrapper<MessageCard>>() {
            @Override
            public void onResponse(Call<RspModelWrapper<MessageCard>> call,
                                   Response<RspModelWrapper<MessageCard>> response) {
                LogUtil.i(TAG, "onResponse -> " + response.raw().toString());
                RspModelWrapper<MessageCard> rspModel = response.body();
                if (rspModel != null && rspModel.success()) {
                    MessageCard result = rspModel.getResult();
                    LogUtil.i(TAG, "result -> " + result.toString());
                    result.setReceived(1);
                    cacheMessage(result);
                    AppUtils.runOnUiThread(() -> {
                        mView.showSendMsgSuccess(result);
                        mView.hideLoadingIndicator();
                    });
                } else {
                    AppUtils.runOnUiThread(() -> {
                        mView.showSendMsgFail(
                                rspModel == null ? "empty response!" : rspModel.getMessage());
                        mView.hideLoadingIndicator();
                    });
                }
            }

            @Override
            public void onFailure(Call<RspModelWrapper<MessageCard>> call,
                                  Throwable t) {
                PresenterHelper.handleFailAction(TAG, t, mView::showActionFail, mView);
            }
        });

    }

    private void cacheMessage(MessageCard card) {
        LanmuDB.saveMessages(card);
    }
}
