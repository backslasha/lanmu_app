package slasha.lanmu.business.main;

import slasha.lanmu.utils.AppUtils;

class MainPresenterImpl implements MainContract.MainPresenter {

    private MainContract.MainView mMainView;
    private MainContract.MainModel mMainModel;

    MainPresenterImpl(MainContract.MainView mainView, MainContract.MainModel mainModel) {
        mMainView = mainView;
        mMainModel = mainModel;
    }

    @Override
    public void performPullBookPostFlow(BookPostFlowFragment.FlowType flowType) {
        mMainView.showLoadingIndicator();
        mMainModel.offerPostFlow(flowType.getType(), bookPostFlow ->
                AppUtils.runOnUiThread(() -> {
                    mMainView.showBookPostFlow(bookPostFlow);
                    mMainView.hideLoadingIndicator();
                })
        );
    }
}
