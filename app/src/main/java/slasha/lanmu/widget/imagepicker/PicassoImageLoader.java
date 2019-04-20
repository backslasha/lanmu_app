package slasha.lanmu.widget.imagepicker;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.imnjh.imagepicker.ImageLoader;
import com.squareup.picasso.Picasso;

import slasha.lanmu.R;
import slasha.lanmu.application.LanmuApplication;

class PicassoImageLoader implements ImageLoader {
    @Override
    public void bindImage(ImageView imageView, Uri uri, int width, int height) {
        Picasso.with(LanmuApplication.instance())
                .load(uri)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .resize(width, height)
                .into(imageView);
    }

    @Override
    public void bindImage(ImageView imageView, Uri uri) {
        Picasso.with(LanmuApplication.instance())
                .load(uri)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;    }

    @Override
    public ImageView createFakeImageView(Context context) {
        return new ImageView(context);
    }
}
