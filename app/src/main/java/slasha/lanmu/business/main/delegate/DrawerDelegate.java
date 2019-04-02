package slasha.lanmu.business.main.delegate;

import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import slasha.lanmu.GlobalBuffer;
import slasha.lanmu.R;
import slasha.lanmu.bean.User;
import slasha.lanmu.business.login.LoginActivity;
import slasha.lanmu.business.main.MainActivity;

public class DrawerDelegate implements NavigationView.OnNavigationItemSelectedListener,
        GlobalBuffer.AccountInfo.UserInfoChangeListener {

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
        mNavigationView.setCheckedItem(R.id.nav_gallery);
        updateNavHeaderUI(GlobalBuffer.AccountInfo.currentUser());
        GlobalBuffer.AccountInfo.registerLoginStatusListener(this);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action            // 使用本地假数据

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

        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLoggedIn(User user) {
        updateNavHeaderUI(user);
    }

    @Override
    public void onLoggedOut() {
        updateNavHeaderUI(null);
    }

    private void jumpToLoginPage() {
        activity.finish();
        activity.startActivity(LoginActivity.newIntent(activity));
    }

    private void updateNavHeaderUI(User user) {
        View root = mNavigationView.getHeaderView(0);
        if (user == null) {
            ((ImageView) root.findViewById(R.id.iv_avatar)).setImageResource(R.mipmap.ic_launcher);
            ((TextView) root.findViewById(R.id.tv_nav_header_title)).setText(R.string.nav_header_title);
            ((TextView) root.findViewById(R.id.tv_nav_header_sub_title))
                    .setText(R.string.nav_header_subtitle);

        } else {
            Picasso.with(activity)
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

    public boolean onBackPressed() {
        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    public void onDestroy() {
        GlobalBuffer.AccountInfo.unregisterLoginStatusListener(this);
    }
}
