package slasha.lanmu;

import androidx.annotation.MainThread;

/**
 * Created by yhb on 18-1-17.
 */

public interface BaseView<T extends BasePresenter> extends LoadingProvider{

    T myPresenter();

    @MainThread
    void showActionFail(String message);
}