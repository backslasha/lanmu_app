package slasha.lanmu.entity.api.message;

import com.google.gson.annotations.Expose;

public class PullMsgModel {
    @Expose
    private long fromId;
    @Expose
    private long toId;
    @Expose
    private int pullCount;

    public static boolean check(PullMsgModel model) {
        return !(model==null);
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public int getPullCount() {
        return pullCount;
    }

    public void setPullCount(int pullCount) {
        this.pullCount = pullCount;
    }
}
