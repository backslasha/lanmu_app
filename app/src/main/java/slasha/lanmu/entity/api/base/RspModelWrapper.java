package slasha.lanmu.entity.api.base;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Date;

public class RspModelWrapper<M> implements Serializable {
    // 成功
    public static final int SUCCEED = 1;
    // 未知错误
    public static final int ERROR_UNKNOWN = 0;

    // 没有找到用户信息
    public static final int ERROR_NOT_FOUND_USER = 4041;
    // 没有找到群信息
    public static final int ERROR_NOT_FOUND_GROUP = 4042;
    // 没有找到群成员信息
    public static final int ERROR_NOT_FOUND_GROUP_MEMBER = 4043;

    // 创建书帖
    public static final int ERROR_CREATE_POST = 3001;
    // 创建群失败
    public static final int ERROR_CREATE_GROUP = 3002;
    // 创建群成员失败
    public static final int ERROR_CREATE_MESSAGE = 3003;

    // 请求参数错误
    public static final int ERROR_PARAMETERS = 4001;
    // 请求参数错误-已存在账户
    public static final int ERROR_PARAMETERS_EXIST_ACCOUNT = 4002;
    // 请求参数错误-已存在名称
    public static final int ERROR_PARAMETERS_EXIST_NAME = 4003;

    // 服务器错误
    public static final int ERROR_SERVICE = 5001;

    // 账户Token错误，需要重新登录
    public static final int ERROR_ACCOUNT_TOKEN = 2001;
    // 账户登录失败
    public static final int ERROR_ACCOUNT_LOGIN = 2002;
    // 账户注册失败
    public static final int ERROR_ACCOUNT_REGISTER = 2003;
    // 没有权限操作
    public static final int ERROR_ACCOUNT_NO_PERMISSION = 2010;

    @Expose
    private int code;
    @Expose
    private String message;
    @Expose
    private Date time;
    @Expose
    private M result;

    public RspModelWrapper() {
        code = 1;
        message = "ok";
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSucceed() {
        return code == SUCCEED;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public M getResult() {
        return result;
    }

    public void setResult(M result) {
        this.result = result;
    }

    public boolean success() {
        return code == SUCCEED;
    }
}