package slasha.lanmu.net;

import android.text.format.DateFormat;
import android.util.Log;

import com.google.gson.Gson;
import com.imnjh.imagepicker.util.LogUtils;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import androidx.annotation.WorkerThread;
import slasha.lanmu.application.LanmuApplication;
import slasha.lanmu.utils.CodecUtils;
import slasha.lanmu.utils.CompressUtils;
import slasha.lanmu.utils.LogUtil;


public class QiniuUploader implements Uploader {

    private static final String DOWNLOAD_URL = "http://pqk956muf.bkt.clouddn.com/";
    private static final String ACCESS_KEY = "uepWdXrdCjjG4PiP-4PYhxFKLiyHO2BU2yUkdUhu";
    private static final String SECRET_KEY = "-ZVr-_wGarn7GokML_LM7tDqKvRuXvoMtCFxh-3Y";
    private static final String BUCKET = "lanmu";
    private static final String TAG = "lanmu.upload";
    private static final long MAX_UPLOAD_IMAGE_LENGTH = 860 * 1024;
    private static final int MAX_UPLOAD_IMAGE_WIDTH = 1920;
    private static final int MAX_UPLOAD_IMAGE_HEIGHT = 1920;

    private UploadManager uploadManager;


    /**
     * eg. image/201703/dawewqfas243rfawr234.jpg
     */
    private static String keyFoyImage(String path) {
        String fileMd5 = CodecUtils.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("image/%s/%s.jpg", dateString, fileMd5);
    }

    /**
     * 分月存储，避免一个文件夹太多
     *
     * @return yyyyMM
     */
    private static String getDateString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }

    @WorkerThread
    @Override
    public String uploadPicture(String path) {
        if (uploadManager == null) {
            Configuration cfg = new Configuration(Zone.autoZone());
            uploadManager = new UploadManager(cfg);
        }

        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        try {
            String key = keyFoyImage(path);

            // 云端已经有此图片
            if (exits(DOWNLOAD_URL + key)) {
                Log.i(TAG, "[upload] remote resource found, skip upload -> " + DOWNLOAD_URL + key);
                return DOWNLOAD_URL + key;
            }

            path = compress(path);

            com.qiniu.http.Response response = uploadManager.put(
                    new File(path),
                    key,
                    auth.uploadToken(BUCKET)
            );

            //解析上传成功的结果
            DefaultPutRet putRet
                    = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

            LogUtils.i(TAG, "[upload] success -> " + DOWNLOAD_URL + putRet.key);

            return DOWNLOAD_URL + putRet.key;

        } catch (Exception ex) {

            LogUtil.e(TAG, ex.getCause().toString());
        }
        return null;
    }

    // todo 在七牛云控制台删除文件后，url 仍然有效，与 七牛云 的 CDN 缓存策略相关，暂无法修改
    private boolean exits(String url) {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            return 200 == con.getResponseCode();
        } catch (Exception ex) {
            return false;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    /**
     * 压缩图片，返回压缩后的临时文件路径
     */
    private String compress(String path) {
        if (path != null) {
            // 进行压缩
            String cacheDir = LanmuApplication.instance().getCacheDir().getAbsolutePath();

            File srcFile = new File(path);
            if (!srcFile.exists()) {
                return path;
            }

            File compressedFile = CompressUtils.compressImage(
                    srcFile,
                    String.format("%s/image/", cacheDir),
                    MAX_UPLOAD_IMAGE_LENGTH,
                    MAX_UPLOAD_IMAGE_WIDTH,
                    MAX_UPLOAD_IMAGE_HEIGHT
            );

            if (compressedFile != null) {
                LogUtil.i(TAG, "[compress] path=" + compressedFile.getAbsolutePath()
                        + ", size: " + srcFile.length() + " -> " + compressedFile.length());
                return compressedFile.getAbsolutePath();
            }

            LogUtil.e(TAG, "[compress] error, path=" + srcFile.getAbsolutePath());
        }
        return path;
    }
}
