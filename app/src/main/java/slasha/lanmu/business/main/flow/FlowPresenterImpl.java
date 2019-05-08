package slasha.lanmu.business.main.flow;

import slasha.lanmu.net.Network;
import slasha.lanmu.utils.PresenterHelper;

class FlowPresenterImpl implements FlowContract.Presenter {

    private FlowContract.View mView;
    private static final String TAG = "lanmu.main";

    FlowPresenterImpl(FlowContract.View view) {
        mView = view;
    }

    @Override
    public void performPullBookPosts(FlowFragment.FlowType flowType) {
        if (flowType == FlowFragment.FlowType.LATEST) {
            PresenterHelper.requestAndHandleResponse(
                    TAG,
                    Network.remote()::latestList,
                    0,
                    mView::showBookPosts,
                    mView::showActionFail,
                    mView
            );

        } else if (flowType == FlowFragment.FlowType.HOT) {
            PresenterHelper.requestAndHandleResponse(
                    TAG,
                    Network.remote()::hotList,
                    0,
                    mView::showBookPosts,
                    mView::showActionFail,
                    mView
            );
        }
    }
}
