package slasha.lanmu.business.search_result;


import java.util.List;

import slasha.lanmu.persistence.Global;
import slasha.lanmu.entity.local.BookPost;
import slasha.lanmu.debug.ArtificialProductFactory;

public class SearchModel implements SearchContract.SearchModel {

    @Override
    public List<BookPost> offer(String keyword) {
        if (Global.Debug.sUseFakeData) {
            return ArtificialProductFactory.bookPosts();
        }

        // TODO: 2019/3/11  
        return null;
    }

}
