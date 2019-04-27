package slasha.lanmu.utils;

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;

import java.io.File;

import slasha.lanmu.utils.common.ImageUtils;

public class CompressUtils {

    /**
     * 压缩图片文件
     * 首先，通过降低图片质量来减少文件长度
     * 当图片质量以及最差时，文件长度仍然超过 maxByteSize 时，
     * 才会使用 sampleSizeWidth、maxHeight 计算 inSampleSize 减少图片宽高
     * 此时最终压缩文件的长度有可能超过 maxByteSize，但是宽高不会超过 maxWidth、maxHeight
     *
     * @param sourceFile  原文件
     * @param dirPath     压缩图片文件存放路径
     * @param maxByteSize 图片文件长度超过此字节数时，反复降低图片质量来缩小文件长度，直到图片质量为 0
     * @param maxWidth    图片允许的最大宽度（像素数）
     * @param maxHeight   图片允许的最大高度
     * @return 生成的压缩图片文件的路径
     */
    public static File compressImage(final File sourceFile,
                                     final String dirPath,
                                     long maxByteSize,
                                     int maxWidth,
                                     int maxHeight) {
        // build source file
        if (sourceFile == null || !sourceFile.exists() || !sourceFile.canRead())
            return null;

        // build source file direct parent directory
        File parentDir = new File(dirPath);
        if (!parentDir.exists()) {
            boolean mkdir = parentDir.mkdir();
            if (!mkdir) {
                Log.e("CompressUtils", "mkdir fail!");
                return null;
            }
        }

        // build to srcBitmap
        Bitmap srcBitmap = ImageUtils.getBitmap(sourceFile);
        if (srcBitmap == null)
            return null;

        // Get the srcBitmap format
        Bitmap.CompressFormat compressFormat = srcBitmap.hasAlpha() ?
                Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG;

        if (sourceFile.length() > maxByteSize) {
            // create new temp file
            File tempFile = new File(
                    dirPath,
                    String.format(
                            "compress_%s.temp",
                            SystemClock.uptimeMillis()
                    ));

            // compress bitmap
            Bitmap compressedBitmap = ImageUtils.compressBySampleSize(
                    ImageUtils.compressByQuality(srcBitmap, maxByteSize, true), // 压缩质量 100 ~ 0
                    maxWidth,
                    maxHeight
            );

            // save compressed bitmap to a tem file
            if (ImageUtils.save(compressedBitmap, tempFile, compressFormat)) {
                return tempFile;
            }
        }

        return sourceFile;
    }


}
