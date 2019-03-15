package slasha.lanmu.business.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;

import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.bean.User;
import slasha.lanmu.business.chat.ChatActivity;
import slasha.lanmu.utils.ToastUtils;

public class UserProfileActivity extends SameStyleActivity implements ProfileContract.ProfileView {

    private static final String EXTRA_USER = "extra_user";
    private User mUser;

    public static Intent newIntent(Context context, User user) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(EXTRA_USER, user);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        mUser = (User) intent.getSerializableExtra(EXTRA_USER);
        showProfile(mUser);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_user_profile;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_start_chat:
                jumpToChatPage();
        }
        return super.onOptionsItemSelected(item);
    }

    private void jumpToChatPage() {
        Intent intent = ChatActivity.newIntent(this, mUser);
        startActivity(intent);
    }

    @Override
    public void showProfile(User user) {
        // TODO: 2019/3/12 show profile
        setTitle(user.getUsername());
    }

    @Override
    public ProfileContract.ProfilePresenter myPresenter() {
        return null;
    }
}
