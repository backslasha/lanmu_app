package slasha.lanmu.business.create_post.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.google.android.material.textfield.TextInputEditText;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import slasha.lanmu.R;
import slasha.lanmu.business.create_post.CreatePostActivity;
import slasha.lanmu.entity.local.Book;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.KeyBoardUtils;
import slasha.lanmu.utils.ToastUtils;

public class BookInfoInputWidget extends ScrollView
        implements CreatePostActivity.ResultListener {

    private final int REQUEST_CODE_IMAGE = 1080;
    private final List<TextInputEditText> mEditTexts;

    @BindView(R.id.iv_selected_image)
    ImageView mIvSelectedImage;

    @BindView(R.id.edt_book_name)
    TextInputEditText mEdtBookName;

    @BindView(R.id.edt_author_name)
    TextInputEditText mEdtAuthorName;

    @BindView(R.id.edt_version)
    TextInputEditText mEdtVersion;

    @BindView(R.id.edt_publish_date)
    TextInputEditText mEdtPublishDate;

    @BindView(R.id.edt_publisher)
    TextInputEditText mEdtPublisher;

    @BindView(R.id.edt_introduction)
    TextInputEditText mEdtIntroduction;

    @BindView(R.id.edt_languish)
    TextInputEditText mEdtLanguish;

    private String mCoverUri;

    public BookInfoInputWidget(Context context) {
        this(context, null);
    }

    public BookInfoInputWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookInfoInputWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater
                .from(context)
                .inflate(R.layout.layout_input_book_info, this, true);

        ButterKnife.bind(this);

        mEditTexts = Arrays.asList(
                mEdtBookName,
                mEdtAuthorName,
                mEdtVersion,
                mEdtPublishDate,
                mEdtPublisher,
                mEdtIntroduction,
                mEdtLanguish
        );
    }

    @OnClick(R.id.fl_add_cover)
    void selectCover() {
        SImagePicker
                .from((Activity) getContext())
                .maxCount(1)
                .rowCount(3)
                .pickMode(SImagePicker.MODE_IMAGE)
                .forResult(REQUEST_CODE_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            if (data != null) {
                final ArrayList<String> pathList =
                        data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
//                final boolean original =
//                        data.getBooleanExtra(PhotoPickerActivity.EXTRA_RESULT_ORIGINAL, false);
                if (!CommonUtils.isEmpty(pathList)) {
                    mIvSelectedImage.setImageURI(
                            Uri.fromFile(new File(mCoverUri = pathList.get(0)))
                    );
                }
            } else {
                ToastUtils.showToast(R.string.select_no_image);
            }

        }
    }

    public Book getBookInfo() {
        Book book = new Book();
        book.setAuthor(String.valueOf(mEdtAuthorName.getText()));
        book.setIntroduction(String.valueOf(mEdtIntroduction.getText()));
        book.setLanguish(String.valueOf(mEdtLanguish.getText()));
        book.setName(String.valueOf(mEdtBookName.getText()));
        book.setPublishDate(String.valueOf(mEdtPublishDate.getText()));
        book.setPublisher(String.valueOf(mEdtPublisher.getText()));
        book.setVersion(String.valueOf(mEdtVersion.getText()));
        return book;
    }

    public boolean allFilled() {
        for (TextInputEditText editText : mEditTexts) {
            if (TextUtils.isEmpty(editText.getText())) {
                return false;
            }
        }
        return true;
    }

    public boolean withCover() {
        return !TextUtils.isEmpty(mCoverUri);
    }

    public void showFieldsNeededTip() {
        View focusView = null;
        for (TextInputEditText editText : mEditTexts) {
            if (TextUtils.isEmpty(editText.getText())) {
                editText.setError(getContext().getString(R.string.error_field_required));
                if (focusView == null) {
                    focusView = editText;
                }
            }
        }
        if (focusView != null)
            focusView.requestFocus();
    }

    public void showCoverNeededTip() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.tip_should_select_cover)
                .setPositiveButton(R.string.general_ok, null)
                .show();
        smoothScrollTo(0, 0);
        hideKeyBoard();
    }

    private void hideKeyBoard() {
        for (TextInputEditText editText : mEditTexts) {
            if (editText.hasFocus()) {
                KeyBoardUtils.closeKeybord(editText);
            }
        }
    }
}
