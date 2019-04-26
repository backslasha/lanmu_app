package slasha.lanmu.business.create_post;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import slasha.lanmu.entity.api.base.RspModelWrapper;
import slasha.lanmu.entity.api.post.CreatePostModel;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.net.Network;
import slasha.lanmu.net.RemoteService;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.LogUtil;

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
        LogUtil.i(TAG, model.toString());
        RemoteService service = Network.remote();
        Call<RspModelWrapper<BookPostCard>> post =
                service.createPost(model);

        mView.showLoadingIndicator();
        post.enqueue(new Callback<RspModelWrapper<BookPostCard>>() {
            @Override
            public void onResponse(Call<RspModelWrapper<BookPostCard>> call,
                                   Response<RspModelWrapper<BookPostCard>> response) {
                RspModelWrapper<BookPostCard> body = response.body();
                AppUtils.runOnUiThread(() -> {
                    if (body.success()) {
                        BookPostCard card = body.getResult();
                        mView.showCreateSuccess(card);
                    } else {
                        mView.showCreateFail(body.getMessage());
                    }
                    mView.hideLoadingIndicator();
                });
            }

            @Override
            public void onFailure(Call<RspModelWrapper<BookPostCard>> call,
                                  Throwable t) {
                AppUtils.runOnUiThread(() -> {
                    mView.showCreateFail(t.getCause().toString());
                    mView.hideLoadingIndicator();
                });
            }
        });
    }
}
