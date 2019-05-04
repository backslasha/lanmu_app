package slasha.lanmu.business.notification;

import android.content.Context;
import android.content.Intent;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;

public class NotificationActivity extends SameStyleActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, NotificationActivity.class);
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
                .add(R.id.fl_container, NotificationFragment.newInstance(), "notification")
                .commit();
    }

    @Override
    protected void initData() {
        setTitle(R.string.title_received_interact);
    }
}

