package slasha.lanmu.entity.card;

import com.google.gson.annotations.Expose;

import java.io.Serializable;


public class UserCard implements Serializable {

    @Expose
    private long id;
    @Expose
    private String name;
    @Expose
    private String avatarUrl;
    @Expose
    private String introduction;
    @Expose
    private String gender;
    @Expose
    private String phone;


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"avatarUrl\":\"")
                .append(avatarUrl).append('\"');
        sb.append(",\"introduction\":\"")
                .append(introduction).append('\"');
        sb.append(",\"gender\":\"")
                .append(gender).append('\"');
        sb.append(",\"phone\":\"")
                .append(phone).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
