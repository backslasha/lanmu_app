package slasha.lanmu.business.profile.edit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.common.LogUtil;
import slasha.lanmu.utils.common.ToastUtils;

public class EditProfileActivity extends SameStyleActivity
        implements EditProfileContract.View {

    private static final String TAG = "lanmu.profile.edit";
    private static final int REQUEST_CODE_PICK_AVATAR = 150;
    private static final String EXTRA_USER_INFO = "extra_user_card";

    @BindView(R.id.iv_avatar)
    ImageView mAvatar;
    @BindView(R.id.iv_sex_tag)
    ImageView mSexTag;
    @BindView(R.id.edt_username)
    EditText mUsername;
    @BindView(R.id.edt_description)
    EditText mDesc;
    @BindView(R.id.edt_phone)
    EditText mPhone;
    @BindView(R.id.btn_submit)
    Button mSubmit;

    private UserInfoCache mUserInfoCache;
    private UserCard mOldCard;
    private EditProfileContract.Presenter mPresenter;

    public static Intent newIntent(Context context, UserCard userCard) {
        Intent intent = new Intent(context, EditProfileActivity.class);
        intent.putExtra(EXTRA_USER_INFO, userCard);
        return intent;
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mOldCard = (UserCard) bundle.getSerializable(EXTRA_USER_INFO);
        if (mOldCard == null) {
            LogUtil.e(TAG, "[EditProfileActivity] empty user card!");
            return false;
        }
        mUserInfoCache = new UserInfoCache(mOldCard);
        return true;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mSubmit.setEnabled(false);
        mUsername.addTextChangedListener(new EditCallback() {
            @Override
            void onTextChanged() {
                mUserInfoCache.name = String.valueOf(mUsername.getText());
                mSubmit.setEnabled(mUserInfoCache.modified());
            }
        });
        mDesc.addTextChangedListener(new EditCallback() {
            @Override
            void onTextChanged() {
                mUserInfoCache.introduction = String.valueOf(mDesc.getText());
                mSubmit.setEnabled(mUserInfoCache.modified());
            }
        });
        mPhone.addTextChangedListener(new EditCallback() {
            @Override
            void onTextChanged() {
                mUserInfoCache.phone = String.valueOf(mPhone.getText());
                mSubmit.setEnabled(mUserInfoCache.modified());
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        Picasso.with(this)
                .load(mOldCard.getAvatarUrl())
                .placeholder(R.drawable.default_holder_add)
                .error(R.drawable.default_holder_add)
                .into(mAvatar);
        updateGenderUI("1".equals(mOldCard.getGender()));
        mUsername.setText(mOldCard.getName());
        mDesc.setText(mOldCard.getIntroduction());
        mPhone.setText(mOldCard.getPhone());
        setTitle("修改信息：" + mOldCard.getName());
    }

    @OnClick(R.id.iv_avatar)
    void onAvatarClick() {
        SImagePicker
                .from(this)
                .maxCount(1)
                .rowCount(3)
                .pickMode(SImagePicker.MODE_IMAGE)
                .forResult(REQUEST_CODE_PICK_AVATAR);
    }


    @OnClick(R.id.iv_sex_tag)
    void onSexTagClick() {
        boolean aBoy = "1".equals(mUserInfoCache.gender);
        if (aBoy) {
            mUserInfoCache.gender = "0";
        } else {
            mUserInfoCache.gender = "1";
        }
        updateGenderUI(!aBoy);
        mSubmit.setEnabled(mUserInfoCache.modified());
    }

    private void updateGenderUI(boolean aBoy) {
        Drawable drawable = getResources()
                .getDrawable(aBoy ? R.drawable.ic_sex_woman : R.drawable.ic_sex_man);
        mSexTag.setImageDrawable(drawable);
        mSexTag.getBackground().setLevel(aBoy ? 0 : 1);// 设置背景的层级，切换颜色
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        if (mUserInfoCache.modified()) {
            myPresenter().performSubmitUserInfo(mUserInfoCache.extract());
        } else {
            mSubmit.setEnabled(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICK_AVATAR) {
            if (data != null) {
                final ArrayList<String> pathList =
                        data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
                if (!CommonUtils.isEmpty(pathList)) {
                    mAvatar.setImageURI(Uri.fromFile(new File(pathList.get(0))));
                    mUserInfoCache.avatarUrl = pathList.get(0);
                }
            } else {
                ToastUtils.showToast(R.string.select_no_image);
            }
            mSubmit.setEnabled(mUserInfoCache.modified());
        }
    }

    @Override
    public void showProfileUpdateSuccess(UserCard user) {
        UserInfo.save2SP(user);
        ToastUtils.showToast(getString(R.string.tip_info_updated));
        finish();
    }

    @Override
    public EditProfileContract.Presenter myPresenter() {
        if (mPresenter == null) {
            mPresenter = new EditProfileImpl(this, this);
        }
        return mPresenter;
    }

    @Override
    public void showLoadingIndicator() {
        showProgressDialog();
    }

    @Override
    public void hideLoadingIndicator() {
        hideProgressDialog();
    }

    @Override
    public void showActionFail(String message) {
        ToastUtils.showToast(getString(R.string.tip_info_update_failed) + message);
    }

    class UserInfoCache {
        private final UserCard sourceCard;
        private String name;
        private String avatarUrl;
        private String introduction;
        private String gender;
        private String phone;

        UserInfoCache(UserCard card) {
            this.sourceCard = card;
            this.name = card.getName();
            this.avatarUrl = card.getAvatarUrl();
            this.introduction = card.getIntroduction();
            this.gender = card.getGender();
            this.phone = card.getPhone();
        }

        boolean modified() {
            return notEquals(name, sourceCard.getName())
                    || notEquals(avatarUrl, sourceCard.getAvatarUrl())
                    || notEquals(introduction, sourceCard.getIntroduction())
                    || notEquals(gender, sourceCard.getGender())
                    || notEquals(phone, sourceCard.getPhone());
        }

        private boolean notEquals(String s1, String s2) {
            if (TextUtils.isEmpty(s1) && TextUtils.isEmpty(s2)) return false;
            if (TextUtils.isEmpty(s1) || TextUtils.isEmpty(s2)) return true;
            return !s1.equals(s2);
        }

        UserCard extract() {
            UserCard newCard = new UserCard();
            newCard.setId(sourceCard.getId());
            newCard.setName(name);
            newCard.setAvatarUrl(avatarUrl);
            newCard.setIntroduction(introduction);
            newCard.setGender(gender);
            newCard.setPhone(phone);
            return newCard;
        }
    }

    abstract class EditCallback implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            onTextChanged();
        }

        abstract void onTextChanged();
    }


}
