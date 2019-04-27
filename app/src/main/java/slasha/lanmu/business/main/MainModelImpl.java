package slasha.lanmu.business.main;

import slasha.lanmu.persistence.Global;
import slasha.lanmu.entity.response.BookPostFlow;
import slasha.lanmu.debug.ArtificialProductFactory;
import slasha.lanmu.utils.common.ThreadUtils;

class MainModelImpl implements MainContract.MainModel {

    @Override
    public void offerPostFlow(int type, Callback<BookPostFlow> postFlowCallback) {
        ThreadUtils.execute(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BookPostFlow bookPostFlow = null;
            // TODO: 2019/3/17  offerPostFlow
            if (Global.Debug.sUserFakeBookPostFlow) {
                bookPostFlow = ArtificialProductFactory.offerPostFlow();
            }
            postFlowCallback.callback(bookPostFlow);
        });
    }
}
