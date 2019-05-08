package slasha.lanmu.business.search_result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import slasha.lanmu.entity.api.base.RspModelWrapper;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.net.Network;
import slasha.lanmu.net.RemoteService;
import slasha.lanmu.utils.PresenterHelper;

public class SearchPresenterImpl implements SearchContract.SearchPresenter {

    private static final String TAG = "lanmu.search";
    private SearchContract.View mView;

    SearchPresenterImpl(SearchContract.View view) {
        mView = view;
    }

    @Override
    public void performQuery(String keyword) {
        RemoteService service = Network.remote();
        Call<RspModelWrapper<List<BookPostCard>>> rspModelWrapperCall
                = service.searchPosts(keyword);
        mView.showLoadingIndicator();
        rspModelWrapperCall.enqueue(new Callback<RspModelWrapper<List<BookPostCard>>>() {
            @Override
            public void onResponse(Call<RspModelWrapper<List<BookPostCard>>> call,
                                   Response<RspModelWrapper<List<BookPostCard>>> response) {
                PresenterHelper.handleSuccessAction(TAG, mView, response, mView::showBookPosts);
            }

            @Override
            public void onFailure(Call<RspModelWrapper<List<BookPostCard>>> call,
                                  Throwable throwable) {
                PresenterHelper.handleFailAction(TAG, mView, throwable);
            }
        });

    }
}