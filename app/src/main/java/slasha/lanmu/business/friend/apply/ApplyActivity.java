package slasha.lanmu.business.friend.apply;

import android.content.Context;
import android.content.Intent;

import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;

public class ApplyActivity extends SameStyleActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, ApplyActivity.class);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_container;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, ApplyFragment.newInstance())
                .commit();
    }

    @Override
    protected void initData() {
        setTitle(R.string.title_applies);
    }

}
