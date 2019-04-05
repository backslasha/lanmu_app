package slasha.lanmu;

import androidx.annotation.MainThread;

/**
 * Created by yhb on 18-1-17.
 */

public interface BaseView<T extends BasePresenter> {

    T myPresenter();

    @MainThread
    void showLoadingIndicator();

    @MainThread
    void hideLoadingIndicator();
}