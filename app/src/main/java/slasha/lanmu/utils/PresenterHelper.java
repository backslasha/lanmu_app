package slasha.lanmu.utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import slasha.lanmu.BaseView;
import slasha.lanmu.entity.api.base.RspModelWrapper;
import slasha.lanmu.utils.common.LogUtil;

public class PresenterHelper {

    public static void handleFailAction(String TAG, BaseView<?> view, Throwable throwable) {
        LogUtil.i(TAG, "onFailure -> " + throwable.getCause());
        AppUtils.runOnUiThread(() -> {
            view.showActionFail(throwable.getMessage());
            view.hideLoadingIndicator();
        });
    }

    public static <T> void handleSuccessAction(String TAG,
                                               BaseView<?> view,
                                               Response<RspModelWrapper<T>> response,
                                               ResponseHandler<T> handler) {
        LogUtil.i(TAG, "onResponse -> " + response.raw().toString());
        RspModelWrapper<T> rspModel = response.body();
        if (rspModel != null && rspModel.success()) {
            T result = rspModel.getResult();
            LogUtil.i(TAG, "result -> " + result.toString());
            AppUtils.runOnUiThread(() -> {
                handler.onFinalResult(result);
                view.hideLoadingIndicator();
            });
        } else {
            AppUtils.runOnUiThread(() -> {
                view.showActionFail(rspModel == null ? "empty response!" : rspModel.getMessage());
                view.hideLoadingIndicator();
            });
        }
    }


    public static <P, R> void requestAndHandleResponse(String TAG,
                                                       BaseView<?> mView,
                                                       RequestHandler<P, R> requestHandler,
                                                       P param,
                                                       ResponseHandler<R> successRspHandler) {
        Call<RspModelWrapper<R>> rspModelWrapperCall = requestHandler.doRequest(param);
        mView.showLoadingIndicator();
        rspModelWrapperCall.enqueue(new Callback<RspModelWrapper<R>>() {
            @Override
            public void onResponse(Call<RspModelWrapper<R>> call,
                                   Response<RspModelWrapper<R>> response) {
                PresenterHelper.handleSuccessAction(
                        TAG,
                        mView,
                        response,
                        successRspHandler
                );
            }

            @Override
            public void onFailure(Call<RspModelWrapper<R>> call,
                                  Throwable t) {
                PresenterHelper.handleFailAction(TAG, mView, t);
            }
        });
    }

    public interface ResponseHandler<R> {
        void onFinalResult(R result);
    }

    public interface RequestHandler<P, R> {
        Call<RspModelWrapper<R>> doRequest(P param);
    }


}
