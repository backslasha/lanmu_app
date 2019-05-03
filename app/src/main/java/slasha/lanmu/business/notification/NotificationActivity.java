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

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_notification;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            private final String[] TITLES = {"评论", "点赞"};

            @Override
            public Fragment getItem(int position) {
                return NotificationFragment.newInstance();
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return TITLES[position];
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {
        setTitle(R.string.title_received_interact);
    }
}

