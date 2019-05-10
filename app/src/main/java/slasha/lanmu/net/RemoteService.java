package slasha.lanmu.net;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import slasha.lanmu.entity.api.account.AccountRspModel;
import slasha.lanmu.entity.api.account.LoginModel;
import slasha.lanmu.entity.api.account.RegisterModel;
import slasha.lanmu.entity.api.base.PageModel;
import slasha.lanmu.entity.api.base.RspModelWrapper;
import slasha.lanmu.entity.api.comment.CreateCommentModel;
import slasha.lanmu.entity.api.comment.CreateReplyModel;
import slasha.lanmu.entity.api.comment.NotifyRspModel;
import slasha.lanmu.entity.api.message.CreateMsgModel;
import slasha.lanmu.entity.api.message.PullMsgModel;
import slasha.lanmu.entity.api.notify.GlobalNotifyRspModel;
import slasha.lanmu.entity.api.post.CreatePostModel;
import slasha.lanmu.entity.card.ApplyCard;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.entity.card.CommentCard;
import slasha.lanmu.entity.card.CommentReplyCard;
import slasha.lanmu.entity.card.UnreadMessagesCard;
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
     * 拉取用户信息
     */
    @GET("user/{userId}/profile")
    Call<RspModelWrapper<UserCard>> searchProfile(@Path("userId") long userId);


    /**
     * 拉取用户创建书帖动态
     */
    @GET("user/dynamic/posts/{userId}")
    Call<RspModelWrapper<List<DynamicCard>>> pullPostsDynamics(@Path("userId") long userId);

    /**
     * 拉取用户回复帖子动态
     */
    @GET("user/dynamic/comments/{userId}")
    Call<RspModelWrapper<List<DynamicCard>>> pullCommentsDynamics(@Path("userId") long userId);

    /**
     * 拉取用户评论动态
     */
    @GET("user/dynamic/replies/{userId}")
    Call<RspModelWrapper<List<DynamicCard>>> pullRepliesDynamics(@Path("userId") long userId);

    /**
     * 拉取用户点赞帖子动态
     */
    @GET("user/dynamic/thumbsup/{userId}")
    Call<RspModelWrapper<List<DynamicCard>>> pullThumbsUpDynamics(@Path("userId") long userId);

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
     * 拉取帖子评论列表
     */
    @GET("comment/{postId}")
    Call<RspModelWrapper<PageModel<CommentCard>>> pullComments(@Path("postId") long postId,
                                                               @Query("order") int order,
                                                               @Query("page") int page);

    /**
     * 拉取评论回复列表
     */
    @GET("comment/reply/{commentId}")
    Call<RspModelWrapper<PageModel<CommentReplyCard>>> pullReplies(@Path("commentId") long commentId,
                                                                @Query("page") int page);

    /**
     * 拉取聊天列表
     */
    @POST("msg/record")
    @Deprecated
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
    @Deprecated
    Call<RspModelWrapper<List<MessageCard>>> pullConversations(@Body long userId);

    /**
     * 拉取未读私信
     */
    @POST("msg/unread")
    Call<RspModelWrapper<List<UnreadMessagesCard>>> pullUnreadMessages(@Body long userId);

    /**
     * 拉取我的帖子评论、评论回复信息
     */
    @GET("comment/resemble/{userId}/")
    Call<RspModelWrapper<NotifyRspModel>> pullNotifications(@Path("userId") long userId);

    /**
     * 点赞评论
     */
    @GET("comment/thumbsup")
    Call<RspModelWrapper> doThumbsUp(@Query("commentId") long commentId,
                                     @Query("fromId") long fromId);

    /**
     * 用户关注接口
     */
    @GET("user/add_friend/{toId}")
    Call<RspModelWrapper<UserCard>> doAddFriend(@Path("toId") long toId, @Query("fromId") long fromId);

    /**
     * 发送好友申请
     */
    @GET("user/apply/{toId}")
    Call<RspModelWrapper> doApply(@Path("toId") long toId, @Query("fromId") long fromId);


    /**
     * 拉取好友申请列表
     */
    @GET("user/applies")
    Call<RspModelWrapper<List<ApplyCard>>> pullApplies(@Query("userId") long userId);


    /**
     * 获取联系人列表
     */
    @GET("user/friends")
    Call<RspModelWrapper<List<UserCard>>> pullFriends(@Query("userId") long userId);


    /**
     * 忽略好友申请
     */
    @POST("user/apply/reject")
    Call<RspModelWrapper<ApplyCard>> doRejectApply(@Body long applyId);


    /**
     * 拉取热门20条
     */
    @GET("posts/hot")
    Call<RspModelWrapper<List<BookPostCard>>> hotList(@Query("dummy") int dummy);

    /**
     * 拉取最新20条
     */
    @GET("posts/latest")
    Call<RspModelWrapper<List<BookPostCard>>> latestList(@Query("dummy") int dummy);

    /**
     * 查询各种未读消息数
     */
    @GET("user/notify_count/{userId}")
    Call<RspModelWrapper<GlobalNotifyRspModel>> globalNotifyCount(@Path("userId") long userId);

}
