package slasha.lanmu.business.create_post;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import slasha.lanmu.R;
import slasha.lanmu.entity.api.base.RspModelWrapper;
import slasha.lanmu.entity.api.post.CreatePostModel;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.net.Network;
import slasha.lanmu.net.QiniuUploader;
import slasha.lanmu.net.RemoteService;
import slasha.lanmu.net.Uploader;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.common.LogUtil;
import slasha.lanmu.utils.common.ThreadUtils;

class CreatePostPresenterImpl implements CreatePostContract.Presenter {

    private static final String TAG = "lanmu.create_post";

    private final CreatePostContract.View mView;
    private final Context mContext;
    private final Uploader mUploader;

    CreatePostPresenterImpl(CreatePostContract.View view, Context context) {
        mView = view;
        mContext = context;
        mUploader = QiniuUploader.getInstance();
    }

    @Override
    public void performCreate(CreatePostModel model) {
        LogUtil.i(TAG, model.toString());
        RemoteService service = Network.remote();
        Call<RspModelWrapper<BookPostCard>> post =
                service.createPost(model);

        mView.showLoadingIndicator();

        uploadImagesFirst(() -> post.enqueue(new Callback<RspModelWrapper<BookPostCard>>() {
            @Override
            public void onResponse(Call<RspModelWrapper<BookPostCard>> call,
                                   Response<RspModelWrapper<BookPostCard>> response) {
                RspModelWrapper<BookPostCard> body = response.body();
                AppUtils.runOnUiThread(() -> {
                    if (body.success()) {
                        BookPostCard card = body.getResult();
                        mView.showCreateSuccess(card);
                    } else {
                        mView.showActionFail(body.getMessage());
                    }
                    mView.hideLoadingIndicator();
                });
            }

            @Override
            public void onFailure(Call<RspModelWrapper<BookPostCard>> call,
                                  Throwable t) {
                AppUtils.runOnUiThread(() -> {
                    mView.showActionFail(t.getCause().toString());
                    mView.hideLoadingIndicator();
                });
            }
        }), model);

    }

    private void uploadImagesFirst(Runnable realCreatePostAction, CreatePostModel model) {
        ThreadUtils.execute(() -> {

            int failCount = 0;

            String bookCoverUrl = model.getBook().getCoverUrl();
            if (!TextUtils.isEmpty(bookCoverUrl)) {
                String realUrl = mUploader.uploadPicture(bookCoverUrl);
                if (realUrl != null) {
                    model.getBook().setCoverUrl(realUrl);
                    LogUtil.i(TAG, "upload cover success -> " + realUrl);
                } else {
                    failCount++;
                    LogUtil.e(TAG, "upload cover fail.");
                }
            }

            List<String> postImageUrls = AppUtils.asUrlList(model.getImages());
            List<String> postImageRealUrls = new ArrayList<>();
            if (failCount == 0) {
                for (String imageUrl : postImageUrls) {
                    String realUrl = mUploader.uploadPicture(imageUrl);
                    if (realUrl != null) {
                        postImageRealUrls.add(realUrl); // collect model's real url
                        LogUtil.i(TAG, "upload cover success -> " + realUrl);
                    } else {
                        failCount++;
                        LogUtil.e(TAG, "upload cover fail.");
                        break;
                    }
                }
            }

            // all images upload finished, create post now
            if (failCount == 0) {
                model.setImages(AppUtils.asOneString(postImageRealUrls)); // update model's urls
                AppUtils.runOnUiThread(realCreatePostAction);
            } else {
                AppUtils.runOnUiThread(() -> {
                    mView.showActionFail(mContext.getString(R.string.tip_upload_image_error));
                    mView.hideLoadingIndicator();
                });
            }

        });
    }
}
