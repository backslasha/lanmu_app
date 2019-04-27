package slasha.lanmu.persistence;

import android.content.Context;
import android.text.TextUtils;

import slasha.lanmu.application.LanmuApplication;
import slasha.lanmu.entity.api.account.AccountRspModel;
import slasha.lanmu.utils.common.SPUtils;

public class AccountInfo {

    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";

    // 登录状态的Token，用来接口请求
    private static String token;
    // 登录的用户ID
    private static String userId;
    // 登录的账户
    private static String account;


    /**
     * 存储数据到XML文件，持久化
     */
    private static void save(Context context) {
        SPUtils.put(context, KEY_TOKEN, token);
        SPUtils.put(context, KEY_USER_ID, userId);
        SPUtils.put(context, KEY_ACCOUNT, account);
    }

    /**
     * 进行数据加载
     */
    public static void load(Context context) {
        token = (String) SPUtils.get(context, KEY_TOKEN, "");
        userId = (String) SPUtils.get(context, KEY_USER_ID, "");
        account = (String) SPUtils.get(context, KEY_ACCOUNT, "");
    }

    public static boolean loggedIn() {
        return !TextUtils.isEmpty(userId) && !TextUtils.isEmpty(token);
    }


    /**
     * 保存当前账户的信息到持久化XML中
     */
    public static void save2SP(AccountRspModel model) {
        // 存储当前登录的账户, token, 用户Id，方便从数据库中查询我的信息
        AccountInfo.token = model.getToken();
        AccountInfo.account = model.getAccount();
        AccountInfo.userId = String.valueOf(model.getUser().getId());
        save(LanmuApplication.instance());
    }


    /**
     * 获取当前登录的Token
     */
    public static String getToken() {
        return token;
    }

    public static void clear(Context context) {
        token = null;
        userId = null;
        account = null;
        SPUtils.remove(context, KEY_TOKEN);
        SPUtils.remove(context, KEY_USER_ID);
        SPUtils.remove(context, KEY_ACCOUNT);
    }

}
