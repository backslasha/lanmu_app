package slasha.lanmu.business.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import slasha.lanmu.GlobalBuffer;
import slasha.lanmu.R;
import slasha.lanmu.bean.User;
import slasha.lanmu.business.login.LoginActivity;
import slasha.lanmu.business.search_result.SearchResultActivity;
import slasha.lanmu.utils.ToastUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GlobalBuffer.AccountInfo.UserInfoChangeListener {

    private SearchView mSearchView;
    private NavigationView mNavigationView;

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(
                view,
                "Replace with your own action",
                Snackbar.LENGTH_LONG
        )
                .setAction("Action", null).show());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setCheckedItem(R.id.nav_gallery);
        updateNavHeaderUI(GlobalBuffer.AccountInfo.currentUser());

        GlobalBuffer.AccountInfo.registerLoginStatusListener(this);
    }

    private void updateNavHeaderUI(User user) {
        View root = mNavigationView.getHeaderView(0);
        if (user == null) {
            ((ImageView) root.findViewById(R.id.iv_avatar)).setImageResource(R.mipmap.ic_launcher);
            ((TextView) root.findViewById(R.id.tv_nav_header_title)).setText(R.string.nav_header_title);
            ((TextView) root.findViewById(R.id.tv_nav_header_sub_title))
                    .setText(R.string.nav_header_subtitle);

        } else {
            Picasso.with(this)
                    .load(user.getAvatarUrl())
                    .into((ImageView) root.findViewById(R.id.iv_avatar));

            ((TextView) root.findViewById(R.id.tv_nav_header_title)).setText(
                    user.getUsername()
            );
//        ((TextView) mNavigationView.findViewById(R.id.tv_nav_header_sub_title)).setText(
//                user.getEmail()
//        );
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setIconifiedByDefault(false);

        // 指定 searchable Activity 信息, 这里就是自己
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null)
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        GlobalBuffer.AccountInfo.unregisterLoginStatusListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            // 使用本地假数据
            GlobalBuffer.Debug.sUseFakeData = true;
        } else if (id == R.id.nav_gallery) {
            GlobalBuffer.Debug.sUseFakeData = false;
        } else if (id == R.id.nav_slideshow) {
            GlobalBuffer.Debug.sUseFakeData = false;
        } else if (id == R.id.nav_manage) {
            GlobalBuffer.Debug.sUseFakeData = false;
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_loggout) {
            jumpToLoginPage();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void jumpToLoginPage() {
        finish();
        startActivity(LoginActivity.newIntent(this));
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
                    SearchResultActivity.newIntent(
                            this,
                            String.valueOf(mSearchView.getQuery())
                    )
            );
        }
    }

    @Override
    public void onLoggedIn(User user) {
        updateNavHeaderUI(user);
    }

    @Override
    public void onLoggedOut() {
        updateNavHeaderUI(null);

    }
}
