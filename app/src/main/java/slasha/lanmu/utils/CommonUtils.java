package slasha.lanmu.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import slasha.lanmu.R;
import slasha.lanmu.application.LanmuApplication;

public class CommonUtils {

    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static <T> boolean isEmpty(T[] list) {
        return list == null || list.length == 0;
    }

    @NonNull
    public static Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888
        );
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    public static void setImage(ImageView view, String url) {
        if (url == null || url.length() == 0) {
            view.setImageResource(R.drawable.default_place_holder);
            return;
        }
        Picasso.with(LanmuApplication.instance())
                .load(url)
                .placeholder(R.drawable.default_place_holder)
                .error(R.drawable.default_place_holder)
                .into(view);
    }

    public static void setAvatar(ImageView view, String url) {
        if (url == null || url.length() == 0) {
            view.setImageResource(R.drawable.ic_default_avatar);
            return;
        }
        Picasso.with(LanmuApplication.instance())
                .load(url)
                .placeholder(R.drawable.default_place_holder)
                .error(R.drawable.ic_default_avatar)
                .into(view);
    }

    public static void setCover(ImageView view, String coverUrl) {
        if (coverUrl == null || coverUrl.length() == 0) {
            view.setImageResource(R.drawable.default_place_holder);
            return;
        }
        Picasso.with(LanmuApplication.instance())
                .load(coverUrl)
                .fit()
                .placeholder(R.drawable.default_place_holder)
                .error(R.drawable.default_place_holder)
                .into(view);
    }

    public static boolean isEmpty(CharSequence text) {
        return text == null || text.length() == 0;
    }
}
