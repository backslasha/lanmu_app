package slasha.lanmu.business.profile.edit;

import android.content.Context;
import android.text.TextUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import slasha.lanmu.R;
import slasha.lanmu.entity.api.base.RspModelWrapper;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.net.Network;
import slasha.lanmu.net.QiniuUploader;
import slasha.lanmu.net.RemoteService;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.PresenterHelper;
import slasha.lanmu.utils.common.ThreadUtils;

class EditProfileImpl implements EditProfileContract.Presenter {

    private static final String TAG = "labmu.profile.edit";
    private EditProfileContract.View mView;
    private Context mContext;

    EditProfileImpl(EditProfileContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void performSubmitUserInfo(UserCard userCard) {
        mView.showLoadingIndicator();
        uploadImagesFirst(() -> {
            RemoteService service = Network.remote();
            Call<RspModelWrapper<UserCard>> rspModelWrapperCall
                    = service.updateProfile(userCard);
            rspModelWrapperCall.enqueue(new Callback<RspModelWrapper<UserCard>>() {
                @Override
                public void onResponse(Call<RspModelWrapper<UserCard>> call,
                                       Response<RspModelWrapper<UserCard>> response) {
                    PresenterHelper.handleSuccessAction(
                            TAG, mView, response, mView::showProfileUpdateSuccess);
                }

                @Override
                public void onFailure(Call<RspModelWrapper<UserCard>> call, Throwable t) {
                    PresenterHelper.handleFailAction(TAG, mView, t);
                }
            });
        }, userCard);
    }

    private void uploadImagesFirst(Runnable realCreatePostAction, UserCard userCard) {
        String avatarUrl = userCard.getAvatarUrl();
        if (TextUtils.isEmpty(avatarUrl) || avatarUrl.startsWith("http")) {
            realCreatePostAction.run();
            return;
        }
        ThreadUtils.execute(() -> {
            String realUrl = QiniuUploader.getInstance().uploadPicture(avatarUrl);
            AppUtils.runOnUiThread(() -> {
                if (TextUtils.isEmpty(realUrl)) {
                    mView.hideLoadingIndicator();
                    mView.showActionFail(mContext
                            .getResources().getString(R.string.tip_upload_image_error));
                } else {
                    userCard.setAvatarUrl(realUrl);
                    realCreatePostAction.run();
                }
            });
        });

    }
}
