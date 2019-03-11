package slasha.lanmu.bean;

public class User {

    private String username;
    private String password;
    private String avatarUrl;

    public User(String username, String password, String avatarUrl) {
        this.username = username;
        this.password = password;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
