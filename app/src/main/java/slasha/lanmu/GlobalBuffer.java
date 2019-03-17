package slasha.lanmu;

import java.util.ArrayList;
import java.util.List;

import slasha.lanmu.bean.User;

public class GlobalBuffer {

    public static class Debug {
        public static boolean sUseFakeData = false;
        public static boolean sUserFakeLoginResult = true;
    }

    public static class AccountInfo {

        private static List<UserInfoChangeListener> sListeners = new ArrayList<>();
        private static User sMe = null;

        public static void registerLoginStatusListener(UserInfoChangeListener listener) {
            sListeners.add(listener);
        }

        public static void unregisterLoginStatusListener(UserInfoChangeListener listener) {
            sListeners.remove(listener);
        }

        public static void rememberLoginUser(User user) {
            sMe = user;
            for (UserInfoChangeListener listener : sListeners) {
                listener.onLoggedIn(sMe);
            }
        }

        public static void forgetLoginUser() {
            sMe = null;
            for (UserInfoChangeListener listener : sListeners) {
                listener.onLoggedOut();
            }
        }

        public static boolean loggedIn() {
            return sMe != null;
        }

        public static User currentUser() {
            return sMe;
        }

        public interface UserInfoChangeListener {
            void onLoggedIn(User user);

            void onLoggedOut();
        }
    }
}
