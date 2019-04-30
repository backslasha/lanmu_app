package slasha.lanmu;

import androidx.annotation.MainThread;

public interface LoadingProvider {
    @MainThread
    void showLoadingIndicator();

    @MainThread
    void hideLoadingIndicator();
}
