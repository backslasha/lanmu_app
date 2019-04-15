package slasha.lanmu.widget.reply;

import android.util.Log;

import slasha.lanmu.GlobalBuffer;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.ThreadUtils;

public class ReplyPublisher implements Publisher {

    private static final String TAG = "ReplyPublisher";
    private ReplyStatusListener mStatusListener;

    public void setStatusListener(ReplyStatusListener statusListener) {
        mStatusListener = statusListener;
    }

    @Override
    public void publishComment(CommentData commentData, String content) {
        ThreadUtils.execute(() -> {
            try {
                Thread.sleep(GlobalBuffer.Debug.sLoadingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                AppUtils.runOnUiThread(() -> {
                    if (mStatusListener != null) {
                        mStatusListener.onPublishFailed();
                        Log.d(TAG,"fail/data=" + commentData + ", content=\"" + content + "\".");
                    }
                });
            }
            AppUtils.runOnUiThread(() -> {
                if (mStatusListener != null) {
                    mStatusListener.onPublishSucceed();
                    Log.d(TAG,"success/data=" + commentData + ", content=\"" + content + "\".");
                }
            });

        });
        AppUtils.runOnUiThread(() -> {
            if (mStatusListener != null) {
                mStatusListener.onPublishStarted();
            }

        });
    }

    @Override
    public void publishCommentReply(CommentReplyData commentReplyData, String content) {
        ThreadUtils.execute(() -> {
            try {
                Thread.sleep(GlobalBuffer.Debug.sLoadingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                AppUtils.runOnUiThread(() -> {
                    if (mStatusListener != null) {
                        mStatusListener.onPublishFailed();
                        Log.d(TAG,"fail/data=" + commentReplyData + ", content=\"" + content + "\".");

                    }
                });
            }
            AppUtils.runOnUiThread(() -> {
                if (mStatusListener != null) {
                    mStatusListener.onPublishSucceed();
                    Log.d(TAG,"success/data=" + commentReplyData + ", content=\"" + content + "\".");
                }
            });

        });
        AppUtils.runOnUiThread(() -> {
            if (mStatusListener != null) {
                mStatusListener.onPublishStarted();
            }
        });
    }


    public interface ReplyStatusListener {
        void onPublishSucceed();

        void onPublishFailed();

        void onPublishStarted();
    }

}

