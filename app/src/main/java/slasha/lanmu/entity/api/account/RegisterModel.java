package slasha.lanmu.entity.api.account;

import com.google.gson.annotations.Expose;

/**
 * 注册时需要的信息
 */
public class RegisterModel {
    @Expose
    private String account;
    @Expose
    private String password;
    @Expose
    private String name;

    public RegisterModel(String account, String password, String name) {
        this.account = account;
        this.password = password;
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
