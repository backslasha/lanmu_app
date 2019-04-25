package slasha.lanmu.business.create_post;

import android.content.Context;

import slasha.lanmu.entity.api.post.CreatePostModel;
import slasha.lanmu.utils.LogUtil;
import slasha.lanmu.utils.ToastUtils;

class CreatePostPresenterImpl implements CreatePostContract.Presenter {

    private static final String TAG = "lanmu.create_post";

    private final CreatePostContract.View mView;
    private final Context mContext;

    CreatePostPresenterImpl(CreatePostContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void performCreate(CreatePostModel model) {
        ToastUtils.showToast("performCreate");
        LogUtil.i(TAG, model.toString());
    }
}
