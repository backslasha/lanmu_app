package slasha.lanmu.persistence;

import java.util.ArrayList;
import java.util.List;

import slasha.lanmu.application.LanmuApplication;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.utils.common.SPUtils;

public class UserInfo {

    private static final String KEY_ID = "sp_key_user";
    private static UserCard self = null;

    public static UserCard self() {
        return self;
    }

    public static long id() {
        return self.getId();
    }

    public static void save2SP(UserCard user) {
        self = user;
        SPUtils.put(LanmuApplication.instance(), KEY_ID, Global.gson().toJson(user));
        for (UserInfoChangeListener listener : sListeners) {
            listener.onUserInfoUpdated(self);
        }
    }

    public static void load() {
        if (self == null) {
            self = Global.gson().fromJson(
                    (String) SPUtils.get(LanmuApplication.instance(), KEY_ID, ""),
                    UserCard.class
            );
            for (UserInfoChangeListener listener : sListeners) {
                listener.onUserInfoLoaded(self);
            }
        }
    }

    public static void clear() {
        self = null;
        SPUtils.remove(LanmuApplication.instance(), KEY_ID);
        for (UserInfoChangeListener listener : sListeners) {
            listener.onUserInfoCleared();
        }
    }

    public interface UserInfoChangeListener {
        /**
         * invoke when user info was loaded from sp
         */
        void onUserInfoLoaded(UserCard user);

        /**
         * invoke when user info was cleared from sp
         */
        void onUserInfoCleared();

        /**
         * invoke when user info was updated/saved to sp
         */
        void onUserInfoUpdated(UserCard user);
    }

    private static List<UserInfoChangeListener> sListeners = new ArrayList<>();

    public static void registerLoginStatusListener(UserInfoChangeListener listener) {
        sListeners.add(listener);
    }

    public static void unregisterLoginStatusListener(UserInfoChangeListener listener) {
        sListeners.remove(listener);
    }
}