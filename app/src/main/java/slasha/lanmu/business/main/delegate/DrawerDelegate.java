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
import slasha.lanmu.persistence.UnreadInfo;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.common.ToastUtils;

public class DrawerDelegate implements NavigationView.OnNavigationItemSelectedListener,
        UserInfo.UserInfoChangeListener, UnreadInfo.UnreadInfoChangeListener {

    private MainActivity activity;
    private NavigationView mNavigationView;
    private Toolbar toolbar;

    public DrawerDelegate(MainActivity activity) {
        this.activity = activity;
    }

    public void delegate() {
        toolbar = activity.findViewById(R.id.toolbar);
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
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        mNavigationView = activity.findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        updateNavHeaderUI(UserInfo.self());
        UserInfo.registerLoginStatusListener(this);
        UnreadInfo.registerLoginStatusListener(this);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_message) {
            AppUtils.jumpToConversationPage(activity);
        } else if (id == R.id.nav_my_contract) {
            AppUtils.jumpToFriendPage(activity);
        } else if (id == R.id.nav_my_notification) {
            AppUtils.jumpToNotificationPage(activity);
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
        root.setOnClickListener(v -> AppUtils.jumpToUserProfile(activity, UserInfo.id()));
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
        UnreadInfo.unregisterLoginStatusListener(this);
    }

    @Override
    public void onApplyCountChanged(int count) {
        updateToolbarIcon();
        updateNavItem(R.id.nav_my_contract, R.string.menu_my_contract, count);
    }

    @Override
    public void onNotificationCountChanged(int count) {
        updateToolbarIcon();
        updateNavItem(R.id.nav_my_notification, R.string.menu_my_notification, count);
    }


    @Override
    public void onMessageCountChanged(int count) {
        updateToolbarIcon();
        updateNavItem(R.id.nav_my_message, R.string.menu_my_message, count);
    }

    private void updateToolbarIcon() {
        if (toolbar != null) {
            if (UnreadInfo.getTotalCount() == 0) {
                toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            } else {
                toolbar.setNavigationIcon(R.drawable.menu_with_badge);
            }
        }
    }

    private void updateNavItem(int menuId, int titleId, int count) {
        MenuItem item = mNavigationView.getMenu().findItem(menuId);
        String rear = String.format(activity.getString(R.string.unread), count);
        String title = activity.getString(titleId);
        if (count > 0) {
            item.setTitle(title + rear);
        } else {
            item.setTitle(title);
        }
    }
}
