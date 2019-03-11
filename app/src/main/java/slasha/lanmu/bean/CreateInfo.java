package slasha.lanmu.bean;

public class CreateInfo {
    private User user;
    private String date;
    private String description;

    public CreateInfo(User user, String date, String description) {
        this.user = user;
        this.date = date;
        this.description = description;
    }

    @Override
    public String toString() {
        return "CreateInfo{" +
                "user=" + user +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

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
