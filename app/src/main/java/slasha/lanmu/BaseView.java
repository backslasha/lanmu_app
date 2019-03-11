package slasha.lanmu;

/**
 * Created by yhb on 18-1-17.
 */

public interface BaseView<T extends BasePresenter> {
    T myPresenter();
}