package slasha.lanmu.business.main;

import slasha.lanmu.GlobalBuffer;
import slasha.lanmu.bean.response.BookPostFlow;
import slasha.lanmu.debug.ArtificialProductFactory;
import slasha.lanmu.utils.ThreadUtils;

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
            if (GlobalBuffer.Debug.sUserFakeBookPostFlow) {
                bookPostFlow = ArtificialProductFactory.offerPostFlow();
            }
            postFlowCallback.callback(bookPostFlow);
        });
    }
}
