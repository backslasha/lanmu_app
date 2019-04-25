package slasha.lanmu.entity.api.account;

import com.google.gson.annotations.Expose;

public class LoginModel {
    @Expose
    private String account;
    @Expose
    private String password;

    public LoginModel(String account, String password) {
        this.account = account;
        this.password = password;
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

    public static boolean check(LoginModel model) {
        return model != null
                && model.account != null
                && model.password != null
                && model.account.length() > 0
                && model.password.length() > 0;
    }

}
