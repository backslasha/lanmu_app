package slasha.lanmu;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (initArgs(getIntent().getExtras())) {
            initWindow();
            int layId = getContentViewResId();
            setContentView(layId);
            initWidget();
            initData();
        } else {
            finish();
        }

    }

    protected void initWindow() {

    }


    /**
     * 初始化相关参数
     *
     * @param bundle 参数Bundle
     * @return 如果参数正确返回True，错误返回False
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 初始化控件
     */
    @CallSuper
    protected void initWidget() {
        ButterKnife.bind(this);

        // common loading spinner
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getLoadingMessage());
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
    }


    /**
     * 初始化数据
     */
    protected void initData() {

    }


    protected String getLoadingMessage() {
        return "";
    }

    public void showProgressDialog() {
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        mProgressDialog.hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    protected abstract int getContentViewResId();

}
