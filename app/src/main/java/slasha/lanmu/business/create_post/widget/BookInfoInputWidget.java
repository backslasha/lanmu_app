package slasha.lanmu.business.create_post.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.google.android.material.textfield.TextInputEditText;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;

import java.io.File;
import java.util.ArrayList;

import slasha.lanmu.R;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.ToastUtils;

public class BookInfoInputWidget extends ScrollView {

    private final int REQUEST_CODE_IMAGE = 1080;

    private FrameLayout mFlAddCover;
    private ImageView mIvSelectedImage;
    private TextInputEditText mEdtBookName;
    private TextInputEditText mEdtAuthorName;
    private TextInputEditText mEdtVersion;
    private TextInputEditText mEdtPublishHouse;
    private TextInputEditText mEdtPublishDate;
    private TextInputEditText mEdtPublisher;

    public BookInfoInputWidget(Context context) {
        this(context, null);
    }

    public BookInfoInputWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookInfoInputWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context)
                .inflate(R.layout.layout_input_book_info, this, true);

        mFlAddCover = findViewById(R.id.fl_add_cover);
        mIvSelectedImage = findViewById(R.id.iv_selected_image);
        mEdtBookName = findViewById(R.id.edt_book_name);
        mEdtAuthorName = findViewById(R.id.edt_author_name);
        mEdtVersion = findViewById(R.id.edt_version);
        mEdtPublishHouse = findViewById(R.id.edt_publish_house);
        mEdtPublishDate = findViewById(R.id.edt_publish_date);
        mEdtPublisher = findViewById(R.id.edt_publisher);

        mFlAddCover.setOnClickListener(v -> {
            SImagePicker
                    .from((Activity) context)
                    .maxCount(1)
                    .rowCount(3)
                    .pickMode(SImagePicker.MODE_IMAGE)
//            .fileInterceptor(new SingleFileLimitInterceptor())
                    .forResult(REQUEST_CODE_IMAGE);
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            if (data != null) {
                final ArrayList<String> pathList =
                        data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
                final boolean original =
                        data.getBooleanExtra(PhotoPickerActivity.EXTRA_RESULT_ORIGINAL, false);
                if (!CommonUtils.isEmpty(pathList)) {
                    mIvSelectedImage.setImageURI(
                            Uri.fromFile(new File(pathList.get(0)))
                    );
                }
            } else {
                ToastUtils.showToast("没有数据");
            }

        }
    }
}
