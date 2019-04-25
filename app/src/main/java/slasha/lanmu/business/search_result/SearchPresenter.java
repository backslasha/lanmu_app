package slasha.lanmu.business.search_result;

import java.util.List;

import slasha.lanmu.entity.local.BookPost;

public class SearchPresenter implements SearchContract.SearchPresenter {

    private SearchContract.SearchModel mSearchModel;
    private SearchContract.SearchView mSearchView;

    SearchPresenter(SearchContract.SearchModel searchModel,
                    SearchContract.SearchView searchView) {
        mSearchModel = searchModel;
        mSearchView = searchView;
    }

    @Override
    public void performQuery(String keyword) {
        List<BookPost> offer = mSearchModel.offer(keyword);
        mSearchView.showBookPosts(offer);
    }
}
