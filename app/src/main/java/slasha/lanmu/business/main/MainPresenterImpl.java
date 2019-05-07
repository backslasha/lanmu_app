package slasha.lanmu.business.main;

import slasha.lanmu.net.Network;
import slasha.lanmu.utils.PresenterHelper;

class MainPresenterImpl implements MainContract.MainPresenter {

    private MainContract.MainView mMainView;
    private static final String TAG = "lanmu.main";

    MainPresenterImpl(MainContract.MainView mainView) {
        mMainView = mainView;
    }

    @Override
    public void performPullBookPosts(BookPostFlowFragment.FlowType flowType) {
        if (flowType == BookPostFlowFragment.FlowType.LATEST) {
            PresenterHelper.requestAndHandleResponse(
                    TAG,
                    Network.remote()::latestList,
                    0,
                    mMainView::showBookPosts,
                    mMainView::showActionFail,
                    mMainView
            );

        } else if (flowType == BookPostFlowFragment.FlowType.HOT) {
            PresenterHelper.requestAndHandleResponse(
                    TAG,
                    Network.remote()::hotList,
                    0,
                    mMainView::showBookPosts,
                    mMainView::showActionFail,
                    mMainView
            );
        }
    }
}
