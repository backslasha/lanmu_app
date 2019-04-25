package slasha.lanmu.entity.api.account;

import com.google.gson.annotations.Expose;

import slasha.lanmu.entity.card.UserCard;


/**
 * 登录/注册成功时返回的基本账户信息
 */
public class AccountRspModel {
    // 用户基本信息
    @Expose
    private UserCard user;
    // 当前登录的账号
    @Expose
    private String account;
    // 当前登录成功后获取的Token,
    // 可以通过Token获取用户的所有信息
    @Expose
    private String token;

    public UserCard getUser() {
        return user;
    }

    public void setUser(UserCard user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "{" + "\"user\":" +
                user +
                ",\"account\":\"" +
                account + '\"' +
                ",\"token\":\"" +
                token + '\"' +
                '}';
    }
}
