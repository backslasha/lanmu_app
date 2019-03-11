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
}
