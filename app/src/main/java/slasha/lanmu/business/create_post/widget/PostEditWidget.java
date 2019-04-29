package slasha.lanmu.business.create_post.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import slasha.lanmu.R;
import slasha.lanmu.business.create_post.CreatePostActivity;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.FormatUtils;
import slasha.lanmu.utils.common.DensityUtils;
import slasha.lanmu.utils.common.ToastUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class PostEditWidget extends ScrollView implements CreatePostActivity.ResultListener {

    private static final int REQUEST_CODE_POST_EDIT_IMAGE = 201;
    private static final int MIN_LENGTH = 32;
    private static final String EMPTY_TEXT = "";

    private RecyclerView mRecyclerView;
    private EditText mEditText;

    private SelectedImageAdapter mSelectedImageAdapter;

    public PostEditWidget(Context context) {
        this(context, null);
    }

    public PostEditWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PostEditWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context)
                .inflate(R.layout.layout_post_edit, this, true);

        mEditText = findViewById(R.id.edt_post_content);
        mSelectedImageAdapter = new SelectedImageAdapter(context);
        mRecyclerView = findViewById(R.id.recycler_view_post_images);
        mRecyclerView.setAdapter(mSelectedImageAdapter);
        mRecyclerView.addItemDecoration(new RecyclerItemDecoration(4, 3));
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_POST_EDIT_IMAGE) {
            if (data != null) {
                final ArrayList<String> pathList =
                        data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
                final boolean original =
                        data.getBooleanExtra(PhotoPickerActivity.EXTRA_RESULT_ORIGINAL, false);
                if (!CommonUtils.isEmpty(pathList)) {
                    mSelectedImageAdapter.performDataSetChanged(pathList);
                }
            } else {
                ToastUtils.showToast(R.string.select_no_image);
            }

        }
    }

    public boolean withEnoughText() {
        if (TextUtils.isEmpty(mEditText.getText())) {
            return false;
        } else
            return mEditText.getText().length() > MIN_LENGTH;
    }

    public String getText() {
        if (TextUtils.isEmpty(mEditText.getText())) {
            return EMPTY_TEXT;
        } else {
            return String.valueOf(mEditText.getText());
        }
    }

    public String getImages() {
        return FormatUtils.asOneString(mSelectedImageAdapter.getEntities());
    }

    public void showTextTooShortTip() {
        ToastUtils.showToast(R.string.tip_text_too_short);
    }


    private class SelectedImageAdapter extends SimpleAdapter<String> {

        SelectedImageAdapter(Context context) {
            super(context);
        }

        @Override
        protected int layoutResId(int viewType) {
            return R.layout.item_removable_image;
        }

        @Override
        protected void bind(SimpleHolder holder, String s) {
            if (s == null) {
                holder.itemView.findViewById(R.id.iv_remove_image).setVisibility(GONE);
                ((ImageView) holder.itemView.findViewById(R.id.iv_removable_image))
                        .setImageResource(R.drawable.add_image);
                holder.setOnClickListener(R.id.iv_removable_image, v -> {
                    SImagePicker
                            .from((Activity) getContext())
                            .maxCount(9)
                            .rowCount(3)
                            .pickMode(SImagePicker.MODE_IMAGE)
//            .fileInterceptor(new SingleFileLimitInterceptor())
                            .setSelected((ArrayList<String>) getEntities())
                            .forResult(REQUEST_CODE_POST_EDIT_IMAGE);
                });

            } else {
                holder.itemView.findViewById(R.id.iv_remove_image).setVisibility(VISIBLE);
                ((ImageView) holder.itemView.findViewById(R.id.iv_removable_image)).setImageURI(
                        Uri.fromFile(new File(s))
                );
                holder.setOnClickListener(R.id.iv_remove_image, v -> {
                    performSingleDataRemoved(s);
                });
            }
        }

        @Override
        public int getItemCount() {
            return Math.min(super.getItemCount() + 1, 9);
        }
    }

    @SuppressWarnings("NullableProblems")
    private class RecyclerItemDecoration extends RecyclerView.ItemDecoration {
        private int itemSpace;
        private int itemNum;

        /**
         * @param itemSpace item间隔
         * @param itemNum   每行item的个数
         */
        public RecyclerItemDecoration(int itemSpace, int itemNum) {
            this.itemSpace = DensityUtils.dp2px(getContext(), itemSpace);
            this.itemNum = DensityUtils.dp2px(getContext(), itemNum);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = itemSpace;
            outRect.right = itemSpace;
        }
    }
}
