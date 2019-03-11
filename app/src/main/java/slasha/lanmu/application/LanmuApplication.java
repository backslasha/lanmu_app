package slasha.lanmu.application;

import android.app.Application;

import slasha.lanmu.utils.ToastUtils;

public class LanmuApplication extends Application {

    private static LanmuApplication sINSTANCE = null;

    @Override
    public void onCreate() {
        super.onCreate();
        if (sINSTANCE == null) {
            sINSTANCE = this;
        }
        doModuleInit();
    }

    private void doModuleInit() {
        ToastUtils.init(this);
    }

    public static LanmuApplication instance() {
        return sINSTANCE;
    }
}
