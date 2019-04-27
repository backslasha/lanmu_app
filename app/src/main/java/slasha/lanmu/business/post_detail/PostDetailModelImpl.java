package slasha.lanmu.business.post_detail;

import java.util.Collections;
import java.util.List;

import slasha.lanmu.persistence.Global;
import slasha.lanmu.entity.local.Comment;
import slasha.lanmu.entity.response.Comments;
import slasha.lanmu.debug.ArtificialProductFactory;
import slasha.lanmu.utils.common.ThreadUtils;

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
            if (Global.Debug.sUserFakeComments) {
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
