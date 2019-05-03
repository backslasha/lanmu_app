package slasha.lanmu.net;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import slasha.lanmu.entity.api.account.AccountRspModel;
import slasha.lanmu.entity.api.account.LoginModel;
import slasha.lanmu.entity.api.account.RegisterModel;
import slasha.lanmu.entity.api.base.RspModelWrapper;
import slasha.lanmu.entity.api.comment.CreateCommentModel;
import slasha.lanmu.entity.api.comment.CreateReplyModel;
import slasha.lanmu.entity.api.comment.NotifyRspModel;
import slasha.lanmu.entity.api.message.CreateMsgModel;
import slasha.lanmu.entity.api.message.PullMsgModel;
import slasha.lanmu.entity.api.post.CreatePostModel;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.entity.card.CommentCard;
import slasha.lanmu.entity.card.CommentReplyCard;
import slasha.lanmu.entity.card.DynamicCard;
import slasha.lanmu.entity.card.MessageCard;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.entity.local.CommentReply;

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

    /**
     * 书名关键字搜索书帖
     */
    @GET("posts/search?type=0")
    Call<RspModelWrapper<List<BookPostCard>>> searchPosts(@Query("value") String keyword);

    /**
     * postId 搜索书帖
     */
    @GET("posts/search?type=1")
    Call<RspModelWrapper<List<BookPostCard>>> searchPostById(@Query("value") long postId);

    /**
     * 修改用户信息
     */
    @POST("account/profile")
    Call<RspModelWrapper<UserCard>> updateProfile(@Body UserCard userCard);

    /**
     * 拉取用户个人动态
     */
    @GET("account/dynamic/{id}")
    Call<RspModelWrapper<List<DynamicCard>>> pullDynamics(@Path("id") long userId);

    /**
     * 发表对帖评论
     */
    @POST("comment/create")
    Call<RspModelWrapper<CommentCard>> createComment(@Body CreateCommentModel model);

    /**
     * 发表对评论回复
     */
    @POST("comment/reply/create")
    Call<RspModelWrapper<CommentReplyCard>> createCommentReply(@Body CreateReplyModel model);

    /**
     * 拉取用户个人动态
     */
    @GET("comment/{postId}")
    Call<RspModelWrapper<List<CommentCard>>> pullComments(@Path("postId") long postId);

    /**
     * 拉取聊天列表
     */
    @POST("msg/record")
    Call<RspModelWrapper<List<MessageCard>>> pullMsgRecords(@Body PullMsgModel model);

    /**
     * 发送消息
     */
    @POST("msg/create")
    Call<RspModelWrapper<MessageCard>> createMsg(@Body CreateMsgModel msgModel);

    /**
     * 拉取最近和20个人的最后一条聊天消息
     */
    @POST("msg/conversations")
    Call<RspModelWrapper<List<MessageCard>>> pullConversations(@Body long userId);


    @GET("comment/resemble/{userId}/")
    Call<RspModelWrapper<NotifyRspModel>> pullNotifications(@Path("userId") long userId);

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
