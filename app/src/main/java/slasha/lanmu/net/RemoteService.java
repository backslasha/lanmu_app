package slasha.lanmu.net;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import slasha.lanmu.entity.api.account.AccountRspModel;
import slasha.lanmu.entity.api.account.LoginModel;
import slasha.lanmu.entity.api.account.RegisterModel;
import slasha.lanmu.entity.api.base.RspModelWrapper;
import slasha.lanmu.entity.api.post.CreatePostModel;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.entity.card.UserCard;

/**
 * 网络请求的所有的接口
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public interface RemoteService {

    /**
     * 注册接口
     *
     * @param model 传入的是RegisterModel
     * @return 返回的是RspModel<AccountRspModel>
     */
    @POST("account/register")
    Call<RspModelWrapper<AccountRspModel>> accountRegister(@Body RegisterModel model);

    /**
     * 登录接口
     *
     * @param model LoginModel
     * @return RspModel<AccountRspModel>
     */
    @POST("account/login")
    Call<RspModelWrapper<AccountRspModel>> accountLogin(@Body LoginModel model);

    /**
     * 创建书帖
     */
    @POST("posts/create")
    Call<RspModelWrapper<BookPostCard>> createPost(@Body CreatePostModel model);

    // 用户搜索的接口
    @GET("user/search/{name}")
    Call<RspModelWrapper<List<UserCard>>> userSearch(@Path("name") String name);

    // 用户关注接口
    @PUT("user/follow/{userId}")
    Call<RspModelWrapper<UserCard>> userFollow(@Path("userId") String userId);

    // 获取联系人列表
    @GET("user/contact")
    Call<RspModelWrapper<List<UserCard>>> userContacts();

    // 查询某人的信息
    @GET("user/{userId}")
    Call<RspModelWrapper<UserCard>> userFind(@Path("userId") String userId);
}
