package slasha.lanmu.net;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import slasha.lanmu.persistence.AccountInfo;
import slasha.lanmu.persistence.Global;

/**
 * 网络请求的封装
 *
 */
public class Network {
    private static Network instance;
    private Retrofit retrofit;
    private OkHttpClient client;

    static {
        instance = new Network();
    }

    private Network() {
    }

    public static OkHttpClient getClient() {
        if (instance.client != null)
            return instance.client;

        // 存储起来
        instance.client = new OkHttpClient.Builder()
                // 给所有的请求添加一个拦截器
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        // 拿到我们的请求
                        Request original = chain.request();
                        // 重新进行build
                        Request.Builder builder = original.newBuilder();
                        if (!TextUtils.isEmpty(AccountInfo.getToken())) {
                            // 注入一个token
                            builder.addHeader("token", AccountInfo.getToken());
                        }
                        builder.addHeader("Content-Type", "application/json");
                        Request newRequest = builder.build();
                        // 返回
                        return chain.proceed(newRequest);
                    }
                })
                .build();
        return instance.client;
    }

    // 构建一个Retrofit
    public static Retrofit getRetrofit() {
        if (instance.retrofit != null)
            return instance.retrofit;

        // 得到一个OK Client
        OkHttpClient client = getClient();

        // Retrofit
        Retrofit.Builder builder = new Retrofit.Builder();

        // 设置电脑链接
        instance.retrofit = builder.baseUrl(Global.API_URL)
                // 设置client
                .client(client)
                // 设置Json解析器
                .addConverterFactory(GsonConverterFactory.create(Global.gson()))
                .build();

        return instance.retrofit;

    }

    /**
     * 返回一个请求代理
     *
     * @return RemoteService
     */
    public static RemoteService remote() {
        return Network.getRetrofit().create(RemoteService.class);
    }

}
