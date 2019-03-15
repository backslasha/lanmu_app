package slasha.lanmu.business.search_result;


import java.util.List;

import slasha.lanmu.GlobalBuffer;
import slasha.lanmu.bean.BookPost;
import slasha.lanmu.debug.ArtificialProductFactory;

public class SearchModel implements SearchContract.SearchModel {

    @Override
    public List<BookPost> offer(String keyword) {
        if (GlobalBuffer.Debug.sUseFakeData) {
            return ArtificialProductFactory.bookPosts();
        }

        // TODO: 2019/3/11  
        return null;
    }

}
