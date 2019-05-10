package slasha.lanmu.business.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import slasha.lanmu.R;
import slasha.lanmu.business.main.delegate.ActionbarDelegate;
import slasha.lanmu.business.main.delegate.DrawerDelegate;
import slasha.lanmu.business.main.flow.FlowFragment;
import slasha.lanmu.business.search_result.ResultActivity;
import slasha.lanmu.entity.api.notify.GlobalNotifyRspModel;
import slasha.lanmu.persistence.UnreadInfo;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.common.LogUtil;

public class MainActivity extends AppCompatActivity implements UnreadMsgContract.View {

    private DrawerDelegate mDrawerDelegate;
    private ActionbarDelegate mActionbarDelegate;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private UnreadMsgContract.Presenter mPresenter;

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionbarDelegate = new ActionbarDelegate(this);
        mActionbarDelegate.delegate();

        mDrawerDelegate = new DrawerDelegate(this);
        mDrawerDelegate.delegate();

        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);

        mViewPager.setAdapter(new FlowPagerAdapter(
                getSupportFragmentManager(), new FlowFragment.FlowType[]{
                FlowFragment.FlowType.HOT,
                FlowFragment.FlowType.LATEST,
        }));
        mTabLayout.setupWithViewPager(mViewPager);
        myPresenter().performQueryGlobalNotifyCount(UserInfo.id());
    }

    @Override
    public void onBackPressed() {
        if (!mDrawerDelegate.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return mActionbarDelegate.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mActionbarDelegate.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDrawerDelegate.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null) {
            return;
        }
        if ("android.intent.action.SEARCH".equals(intent.getAction())) {
            startActivity(ResultActivity.newIntent(this,
                    String.valueOf(mActionbarDelegate.getSearchView().getQuery())));
        }
    }

    @Override
    public void showPullGlobalNotifyCountSuccess(GlobalNotifyRspModel model) {
        UnreadInfo.setUnreadInfo(model);
    }

    @Override
    public UnreadMsgContract.Presenter myPresenter() {
        if (mPresenter == null) {
            mPresenter = new UnreadMsgPresenterImpl(this);
        }
        return mPresenter;
    }

    @Override
    public void showActionFail(String message) {
        LogUtil.e("拉取未读消息失败：" + message);
    }

    @Override
    public void showLoadingIndicator() {

    }

    @Override
    public void hideLoadingIndicator() {

    }

    public class FlowPagerAdapter extends FragmentPagerAdapter {

        private FlowFragment.FlowType[] mBookPostFlows;

        FlowPagerAdapter(FragmentManager fm, FlowFragment.FlowType[] flowTypes) {
            super(fm);
            mBookPostFlows = flowTypes;
        }

        @Override
        public Fragment getItem(int position) {
            FlowFragment flowFragment = FlowFragment.newInstance(
                    CommonUtils.isEmpty(mBookPostFlows) ? null : mBookPostFlows[position]
            );
            flowFragment.setFlowRefreshListener(()
                    -> myPresenter().performQueryGlobalNotifyCount(UserInfo.id()));

            return flowFragment;
        }

        @Override
        public int getCount() {
            return CommonUtils.isEmpty(mBookPostFlows) ? 0 : mBookPostFlows.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mBookPostFlows[position].getName();
        }
    }
}
