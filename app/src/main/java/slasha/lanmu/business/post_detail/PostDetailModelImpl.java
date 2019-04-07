package slasha.lanmu.business.post_detail;

import java.util.Collections;
import java.util.List;

import slasha.lanmu.GlobalBuffer;
import slasha.lanmu.bean.Comment;
import slasha.lanmu.bean.response.Comments;
import slasha.lanmu.debug.ArtificialProductFactory;
import slasha.lanmu.utils.ThreadUtils;

class PostDetailModelImpl implements PostDetailContract.PostDetailModel {
    @Override
    public void offerComments(long postId, Callback<List<Comment>> listCallback) {
        ThreadUtils.execute(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Comments comments = null;
            // TODO: 2019/3/17  offerPostFlow
            if (GlobalBuffer.Debug.sUserFakeComments) {
                comments = ArtificialProductFactory.offerComments();
            }
            if (comments != null) {
                listCallback.callback(comments.getComments());
            } else {
                listCallback.callback(Collections.emptyList());
            }
        });
    }
}
