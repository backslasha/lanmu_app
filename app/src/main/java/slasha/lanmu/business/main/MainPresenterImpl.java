package slasha.lanmu.business.main;

import slasha.lanmu.GlobalBuffer;
import slasha.lanmu.bean.BookPostFlow;
import slasha.lanmu.debug.ArtificialProductFactory;

class MainPresenterImpl implements MainContract.MainPresenter {

    private MainContract.MainView mMainView;
    private MainContract.MainModel mMainModel;

    MainPresenterImpl(MainContract.MainView mainView, MainContract.MainModel mainModel) {
        mMainView = mainView;
        mMainModel = mainModel;
    }

    @Override
    public void performPullBookPostFlow() {
        BookPostFlow bookPostFlow;
        if (GlobalBuffer.Debug.sUserFakeBookPostFlow) {
            bookPostFlow = ArtificialProductFactory.offerPostFlow();
        } else {
            bookPostFlow = mMainModel.offerPostFlow();
        }
        mMainView.showBookPostFlow(bookPostFlow);
    }
}
