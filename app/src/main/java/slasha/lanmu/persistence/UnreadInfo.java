package slasha.lanmu.persistence;

import java.util.ArrayList;
import java.util.List;

import slasha.lanmu.entity.api.notify.GlobalNotifyRspModel;

public class UnreadInfo {

    private static final GlobalNotifyRspModel mGlobalNotifyRspModel = new GlobalNotifyRspModel();

    public static int getApplyCount() {
        return mGlobalNotifyRspModel.getApplyCount();
    }

    public static void setApplyCount(int applyCount) {
        mGlobalNotifyRspModel.setApplyCount(applyCount);
        for (UnreadInfoChangeListener listener : sListeners) {
            listener.onApplyCountChanged(applyCount);
        }
    }

    public static int getMessageCount() {
        return mGlobalNotifyRspModel.getMessageCount();
    }

    public static void setMessageCount(int messageCount) {
        mGlobalNotifyRspModel.setMessageCount(messageCount);
        for (UnreadInfoChangeListener listener : sListeners) {
            listener.onMessageCountChanged(messageCount);
        }
    }

    public static int getCommentCount() {
        return mGlobalNotifyRspModel.getCommentCount();
    }

    public static void setCommentCount(int commentCount) {
        mGlobalNotifyRspModel.setCommentCount(commentCount);
        for (UnreadInfoChangeListener listener : sListeners) {
            listener.onNotificationCountChanged(commentCount + getThumbsUpCount());
        }
    }

    public static int getThumbsUpCount() {
        return mGlobalNotifyRspModel.getThumbsUpCount();
    }

    public static void setThumbsUpCount(int thumbsUpCount) {
        mGlobalNotifyRspModel.setThumbsUpCount(thumbsUpCount);
        for (UnreadInfoChangeListener listener : sListeners) {
            listener.onNotificationCountChanged(thumbsUpCount + getCommentCount());
        }
    }

    private static List<UnreadInfoChangeListener> sListeners = new ArrayList<>();

    public static void registerLoginStatusListener(UnreadInfoChangeListener listener) {
        sListeners.add(listener);
    }

    public static void unregisterLoginStatusListener(UnreadInfoChangeListener listener) {
        sListeners.remove(listener);
    }

    public static void setUnreadInfo(GlobalNotifyRspModel model) {
        mGlobalNotifyRspModel.setApplyCount(model.getApplyCount());
        mGlobalNotifyRspModel.setMessageCount(model.getMessageCount());
        mGlobalNotifyRspModel.setCommentCount(model.getCommentCount());
        mGlobalNotifyRspModel.setThumbsUpCount(model.getThumbsUpCount());
        for (UnreadInfoChangeListener listener : sListeners) {
            listener.onApplyCountChanged(getApplyCount());
            listener.onNotificationCountChanged(getCommentCount() + getThumbsUpCount());
            listener.onMessageCountChanged(getMessageCount());
        }
    }

    public static int getTotalCount() {
        return getApplyCount() + getCommentCount() + getMessageCount() + getThumbsUpCount();
    }

    public interface UnreadInfoChangeListener {
        void onApplyCountChanged(int count);

        void onNotificationCountChanged(int count);

        void onMessageCountChanged(int count);
    }
}
