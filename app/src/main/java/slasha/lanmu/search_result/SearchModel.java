package slasha.lanmu.search_result;


import java.util.List;

import slasha.lanmu.bean.BookPost;

public class SearchModel implements SearchContract.SearchModel<BookPost> {

    @Override
    public List<BookPost> offer(String keyword) {
        return null;
    }
}
