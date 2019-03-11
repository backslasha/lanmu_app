
package slasha.lanmu.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateInfo {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("description")
    @Expose
    private String description;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
