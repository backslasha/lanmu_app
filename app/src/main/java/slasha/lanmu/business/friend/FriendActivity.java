package slasha.lanmu.business.friend;

import android.content.Context;
import android.content.Intent;

import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;

public class FriendActivity extends SameStyleActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, FriendActivity.class);
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
                .add(R.id.fl_container, FriendFragment.newInstance())
                .commit();
    }

    @Override
    protected void initData() {
        setTitle(R.string.title_friends);
    }

}
