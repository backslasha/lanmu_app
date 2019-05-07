package slasha.lanmu.utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import slasha.lanmu.BaseView;
import slasha.lanmu.LoadingProvider;
import slasha.lanmu.entity.api.base.RspModelWrapper;
import slasha.lanmu.utils.common.LogUtil;

public class PresenterHelper {

    @Deprecated
    public static void handleFailAction(String TAG, BaseView<?> view, Throwable throwable) {
        LogUtil.i(TAG, "onFailure -> " + throwable.getMessage());
        AppUtils.runOnUiThread(() -> {
            view.showActionFail(throwable.getMessage());
            view.hideLoadingIndicator();
        });
    }

    @Deprecated
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


    /**
     * 处理常规的业务请求流程，包括 发请求、收请求、处理响应、回调 View 层的成功/失败、
     * 显示/取消加载符号、打印 log
     * @param TAG 打印 log 使用的 TAG
     * @param requestHandler 发送 retrofit 请求使用的函数
     * @param param retrofit 请求函数使用的参数
     * @param successRspHandler 响应成功解析到 result 时回调的函数
     * @param failRspHandler 响应解析失败时回调的函数
     * @param loadingProvider 提供显示/取消指示符的接口，其实现直接控制 View 层
     * @param <Param> // retrofit 请求函数使用的参数类型
     * @param <Result> // 响应成功解析到 result 的类型
     */
    public static <Param, Result> void requestAndHandleResponse(
            String TAG, RequestHandler<Param, Result> requestHandler, Param param,
            ResponseHandler<Result> successRspHandler,
            ResponseHandler<String> failRspHandler,
            LoadingProvider loadingProvider) {

        Call<RspModelWrapper<Result>> rspModelWrapperCall = requestHandler.doRequest(param);
        loadingProvider.showLoadingIndicator();
        rspModelWrapperCall.enqueue(new Callback<RspModelWrapper<Result>>() {
            @Override
            public void onResponse(Call<RspModelWrapper<Result>> call,
                                   Response<RspModelWrapper<Result>> response) {
                PresenterHelper.handleSuccessAction(
                        TAG,
                        response,
                        successRspHandler,
                        failRspHandler,
                        loadingProvider
                );
            }

            @Override
            public void onFailure(Call<RspModelWrapper<Result>> call, Throwable t) {
                PresenterHelper.handleFailAction(TAG, t, failRspHandler, loadingProvider);
            }
        });
    }


    /**
     * 处理常规的业务请求流程，包括 发请求、收请求、处理响应、回调 View 层的成功/失败、
     * 显示/取消加载符号、打印 log
     * @param TAG 打印 log 使用的 TAG
     * @param call 发送 retrofit 请求得到 call
     * @param successRspHandler 响应成功解析到 result 时回调的函数
     * @param failRspHandler 响应解析失败时回调的函数
     * @param loadingProvider 提供显示/取消指示符的接口，其实现直接控制 View 层
     * @param <Result> // 响应成功解析到 result 的类型
     */
    public static <Result> void requestAndHandleResponse(
            String TAG, Call<RspModelWrapper<Result>> call,
            ResponseHandler<Result> successRspHandler,
            ResponseHandler<String> failRspHandler,
            LoadingProvider loadingProvider) {

        loadingProvider.showLoadingIndicator();
        call.enqueue(new Callback<RspModelWrapper<Result>>() {
            @Override
            public void onResponse(Call<RspModelWrapper<Result>> call,
                                   Response<RspModelWrapper<Result>> response) {
                PresenterHelper.handleSuccessAction(
                        TAG,
                        response,
                        successRspHandler,
                        failRspHandler,
                        loadingProvider
                );
            }

            @Override
            public void onFailure(Call<RspModelWrapper<Result>> call, Throwable t) {
                PresenterHelper.handleFailAction(TAG, t, failRspHandler, loadingProvider);
            }
        });
    }


    public static <Result> void handleSuccessAction(String TAG,
                                                     Response<RspModelWrapper<Result>> response,
                                                     ResponseHandler<Result> successRspHandler,
                                                     ResponseHandler<String> exceptionHandler,
                                                     LoadingProvider loadingProvider) {

        LogUtil.i(TAG, "onResponse -> " + response.raw().toString());
        RspModelWrapper<Result> rspModel = response.body();
        if (rspModel != null && rspModel.success()) {
            Result result = rspModel.getResult();
            LogUtil.i(TAG, "result -> " + result.toString());
            AppUtils.runOnUiThread(() -> {
                successRspHandler.onFinalResult(result);
                loadingProvider.hideLoadingIndicator();
            });
        } else {
            AppUtils.runOnUiThread(() -> {
                exceptionHandler.onFinalResult(
                        rspModel == null ? "empty response!" : rspModel.getMessage());
                loadingProvider.hideLoadingIndicator();
            });
        }
    }

    public static void handleFailAction(String TAG, Throwable throwable,
                                         ResponseHandler<String> failRspHandler,
                                         LoadingProvider loadingProvider) {
        LogUtil.i(TAG, "onFailure -> " + throwable.getMessage());
        AppUtils.runOnUiThread(() -> {
            failRspHandler.onFinalResult(throwable.getMessage());
            loadingProvider.hideLoadingIndicator();
        });
    }

    public interface ResponseHandler<R> {
        void onFinalResult(R result);
    }


    public interface RequestHandler<P, R> {
        Call<RspModelWrapper<R>> doRequest(P param);
    }
}
