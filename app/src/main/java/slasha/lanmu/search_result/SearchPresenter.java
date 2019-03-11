package slasha.lanmu.search_result;

import java.util.List;

import slasha.lanmu.bean.BookPost;

public class SearchPresenter implements SearchContract.SearchPresenter {

    private SearchContract.SearchModel<BookPost> mSearchModel;
    private SearchContract.SearchView mSearchView;

    SearchPresenter(SearchContract.SearchModel<BookPost> searchModel,
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
