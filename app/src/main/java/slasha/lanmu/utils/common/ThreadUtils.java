package slasha.lanmu.utils.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {

    private static ExecutorService sExecutor = Executors.newCachedThreadPool();

    public static void execute(Runnable runnable, Listener listener) {
        sExecutor.execute(new RunnableWrapper(runnable, listener));
    }

    public static void execute(Runnable runnable) {
        sExecutor.execute(runnable);
    }

    public interface Listener {
        void onCommandFinished();
    }

    public static class RunnableWrapper implements Runnable {

        RunnableWrapper(Runnable runnable, Listener listener) {
            mRunnable = runnable;
            mListener = listener;
        }

        private Runnable mRunnable;
        private Listener mListener;

        @Override
        public void run() {
            mRunnable.run();
            if (mListener != null) {
                mListener.onCommandFinished();
            }
        }
    }
}