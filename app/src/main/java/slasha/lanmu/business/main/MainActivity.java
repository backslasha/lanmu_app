package slasha.lanmu.business.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import slasha.lanmu.R;
import slasha.lanmu.business.main.delegate.ActionbarDelegate;
import slasha.lanmu.business.main.delegate.DrawerDelegate;
import slasha.lanmu.business.search_result.ResultActivity;
import slasha.lanmu.utils.common.ToastUtils;

public class MainActivity extends AppCompatActivity {

    private DrawerDelegate mDrawerDelegate;
    private ActionbarDelegate mActionbarDelegate;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;


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

        mViewPager.setAdapter(new BookPostFlowPagerAdapter(
                getSupportFragmentManager(), new BookPostFlowFragment.FlowType[]{
                        BookPostFlowFragment.FlowType.HOT,
                        BookPostFlowFragment.FlowType.LATEST,
        }));
        mTabLayout.setupWithViewPager(mViewPager);
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
            ToastUtils.showToast("intent.getAction():" + intent.getAction());
            startActivity(
                    ResultActivity.newIntent(
                            this,
                            String.valueOf(mActionbarDelegate.getSearchView().getQuery())
                    )
            );
        }
    }

}
