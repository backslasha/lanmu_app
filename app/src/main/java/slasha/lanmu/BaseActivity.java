package slasha.lanmu;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());

        // common loading spinner
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getLoadingMessage());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
    }

    protected String getLoadingMessage() {
        return "";
    }

    protected void showProgressDialog() {
        mProgressDialog.show();
    }

    protected void hideProgressDialog() {
        mProgressDialog.hide();
    }

    protected abstract int getContentViewResId();

}
