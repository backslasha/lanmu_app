package slasha.lanmu.business.main.delegate;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import slasha.lanmu.R;
import slasha.lanmu.business.account.AccountActivity;
import slasha.lanmu.business.main.MainActivity;
import slasha.lanmu.entity.card.UserCard;
import slasha.lanmu.persistence.AccountInfo;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.common.ToastUtils;

public class DrawerDelegate implements NavigationView.OnNavigationItemSelectedListener,
        UserInfo.UserInfoChangeListener {

    private MainActivity activity;
    private NavigationView mNavigationView;

    public DrawerDelegate(MainActivity activity) {
        this.activity = activity;
    }

    public void delegate() {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = activity.findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setCheckedItem(R.id.nav_main_page);
        updateNavHeaderUI(UserInfo.self());
        UserInfo.registerLoginStatusListener(this);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main_page) {

        } else if (id == R.id.nav_my_message) {
            AppUtils.jumpToCoversationPage(activity);
        } else if (id == R.id.nav_my_contract) {
            ToastUtils.showToast("todo");
        } else if (id == R.id.nav_setting) {
            ToastUtils.showToast("todo");
        } else if (id == R.id.nav_share) {
            ToastUtils.showToast("todo");
        } else if (id == R.id.nav_logout) {
            // 清除账户信息
            AccountInfo.clear(activity);
            UserInfo.clear();
            jumpToLoginPage();
        }

        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onUserInfoLoaded(UserCard user) {
        updateNavHeaderUI(user);
    }

    @Override
    public void onUserInfoCleared() {
        updateNavHeaderUI(null);
    }

    @Override
    public void onUserInfoUpdated(UserCard user) {
        updateNavHeaderUI(user);
    }

    private void jumpToLoginPage() {
        activity.finish();
        activity.startActivity(AccountActivity.newIntent(activity));
    }

    private void updateNavHeaderUI(UserCard user) {
        View root = mNavigationView.getHeaderView(0);
        root.setOnClickListener(v -> AppUtils.jumpToUserProfile(activity, UserInfo.self()));
        if (user == null) {
            ((ImageView) root.findViewById(R.id.iv_avatar)).setImageResource(R.mipmap.ic_launcher);
            ((TextView) root.findViewById(R.id.tv_nav_header_title)).setText(R.string.nav_header_title);
            ((TextView) root.findViewById(R.id.tv_nav_header_sub_title))
                    .setText(R.string.nav_header_subtitle);

        } else {
            CommonUtils.setAvatar(root.findViewById(R.id.iv_avatar),
                    user.getAvatarUrl());
            ((TextView) root.findViewById(R.id.tv_nav_header_title)).setText(
                    user.getName()
            );
            ((TextView) root.findViewById(R.id.tv_nav_header_sub_title)).setText(
                    user.getPhone()
            );
        }
    }

    public boolean onBackPressed() {
        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    public void onDestroy() {
        UserInfo.unregisterLoginStatusListener(this);
    }
}
