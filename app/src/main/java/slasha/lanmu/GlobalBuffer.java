package slasha.lanmu;

import slasha.lanmu.bean.User;
import slasha.lanmu.debug.ArtificialProductFactory;

public class GlobalBuffer {
    public static class Debug {
        public static boolean sUseFakeData = false;
        static boolean sMockLoggedIn = true;
    }

    public static class Profile {

        private static User sMe = null;

        public static void rememberLoginUser(User user) {
            sMe = user;
        }

        public static void forgetLoginUser() {
            sMe = null;
        }

        public static boolean loggedIn() {
            if (Debug.sMockLoggedIn) {
                return true;
            }
            return sMe != null;
        }

        public static User currentUser() {
            if (Debug.sMockLoggedIn) {
                return ArtificialProductFactory.user();
            }
            return sMe;
        }
    }
}
