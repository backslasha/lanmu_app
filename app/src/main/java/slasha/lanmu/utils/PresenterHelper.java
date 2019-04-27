package slasha.lanmu.utils;

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
                                               DetailHandler<T> handler) {
        LogUtil.i(TAG, "onResponse -> " + response.raw().toString());
        RspModelWrapper<T> rspModel = response.body();
        if (rspModel != null && rspModel.success()) {
            T result = rspModel.getResult();
            AppUtils.runOnUiThread(() -> {
                handler.onDetail(result);
                view.hideLoadingIndicator();
            });
        } else {
            AppUtils.runOnUiThread(() -> {
                view.showActionFail(rspModel == null ? "empty response!" : rspModel.getMessage());
                view.hideLoadingIndicator();
            });
        }
    }

    public interface DetailHandler<T> {
        void onDetail(T result);
    }


}
