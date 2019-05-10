package slasha.lanmu.entity.api.base;

import java.util.List;

public class UnreadModel<T> {

    private List<T> result;
    private int unreadCount;

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public UnreadModel(List<T> result, int unreadCount) {
        this.result = result;
        this.unreadCount = unreadCount;
    }
}
