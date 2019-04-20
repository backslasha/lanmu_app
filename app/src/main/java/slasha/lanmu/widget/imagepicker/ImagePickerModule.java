package slasha.lanmu.widget.imagepicker;


import android.content.Context;

import com.imnjh.imagepicker.PickerConfig;
import com.imnjh.imagepicker.SImagePicker;

import slasha.lanmu.R;

/**
 * https://github.com/martin90s/ImagePicker
 */
public class ImagePickerModule {

    public static void init(Context context) {
        SImagePicker.init(new PickerConfig.Builder().setAppContext(context)
                .setImageLoader(new PicassoImageLoader())
                .setToolbaseColor(context.getResources().getColor(R.color.colorPrimary))
                .build());
    }
}
