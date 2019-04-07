package slasha.lanmu.bean.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import slasha.lanmu.bean.User;

public class LoginResult {

    @Expose
    @SerializedName("user")
    private User user;
    @Expose
    @SerializedName("status")
    private int status;

    @Override
    public String toString() {
        return "LoginResult{" +
                "user=" + user +
                ", status=" + status +
                '}';
    }

    public LoginResult(User user, int status) {
        this.user = user;
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
