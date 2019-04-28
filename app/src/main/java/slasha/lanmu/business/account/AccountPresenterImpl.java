package slasha.lanmu.business.account;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import slasha.lanmu.entity.api.account.AccountRspModel;
import slasha.lanmu.entity.api.account.LoginModel;
import slasha.lanmu.entity.api.account.RegisterModel;
import slasha.lanmu.entity.api.base.RspModelWrapper;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.entity.local.User;
import slasha.lanmu.net.Network;
import slasha.lanmu.net.RemoteService;
import slasha.lanmu.persistence.AccountInfo;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.common.LogUtil;

public class AccountPresenterImpl implements AccountContract.Presenter {

    private static final String TAG = "lanmu.account";
    private AccountContract.View mView;
    private Context mContext;

    public AccountPresenterImpl(AccountContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void performLogin(LoginModel model) {
        RemoteService service = Network.remote();
        Call<RspModelWrapper<AccountRspModel>> wrapperCall
                = service.accountLogin(model);
        mView.showLoadingIndicator();
        wrapperCall.enqueue(new AccountRspModelCallback());

    }

    @Override
    public void performRegister(RegisterModel model) {
        RemoteService service = Network.remote();
        Call<RspModelWrapper<AccountRspModel>> wrapperCall
                = service.accountRegister(model);
        mView.showLoadingIndicator();
        wrapperCall.enqueue(new AccountRspModelCallback());

    }

    private void saveAccountInfo(AccountRspModel accountRspModel) {
        AccountInfo.save2SP(accountRspModel); // 同步到XML持久化中
    }

    private void saveUserInfo(UserCard user) {
        UserInfo.save2SP(user);
    }

    class AccountRspModelCallback implements Callback<RspModelWrapper<AccountRspModel>> {
        @Override
        public void onResponse(Call<RspModelWrapper<AccountRspModel>> call,
                               Response<RspModelWrapper<AccountRspModel>> response) {
            LogUtil.i(TAG, "onResponse -> " + response.raw().toString());
            RspModelWrapper<AccountRspModel> rspModel = response.body();
            if (rspModel.success()) {
                AccountRspModel accountRspModel = rspModel.getResult();
                UserCard user = accountRspModel.getUser();
                saveAccountInfo(accountRspModel);
                saveUserInfo(user);
                AppUtils.runOnUiThread(() -> {
                    mView.showActionSuccess(user);
                    mView.hideLoadingIndicator();
                });
            } else {
                AppUtils.runOnUiThread(() -> {
                    mView.showActionFail(rspModel.getMessage());
                    mView.hideLoadingIndicator();
                });
            }
        }

        @Override
        public void onFailure(Call<RspModelWrapper<AccountRspModel>> call,
                              Throwable throwable) {
            LogUtil.i(TAG, "onFailure -> " + throwable.getCause());
            AppUtils.runOnUiThread(() -> {
                mView.showActionFail(throwable.getMessage());
                mView.hideLoadingIndicator();
            });
        }
    }


}
