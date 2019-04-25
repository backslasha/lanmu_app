package slasha.lanmu.entity.local;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import slasha.lanmu.entity.card.UserCard;

public class User implements Serializable {

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

    public User() {

    }

    public User(UserCard card) {
        this.id = card.getId();
        this.name = card.getName();
        this.avatarUrl = card.getAvatarUrl();
        this.introduction = card.getIntroduction();
        this.gender = card.getGender();
        this.phone = card.getPhone();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

}
