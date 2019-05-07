package slasha.lanmu.business.profile.dynamic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.entity.card.DynamicCard;
import slasha.lanmu.utils.common.LogUtil;

public class DynamicActivity extends SameStyleActivity {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    private static final String TAG = "lanmu.dynamic";
    private static final String EXTRA_USER_ID = "extra_user_id";
    private long mUserId;

    public static Intent newIntent(Context context, long userId) {
        Intent intent = new Intent(context, DynamicActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        return intent;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mUserId = bundle.getLong(EXTRA_USER_ID, -1);
        if (-1 == mUserId) {
            LogUtil.e(TAG, "[DynamicActivity] -1 user id!");
            return false;
        }
        return true;
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_container_with_viewpager;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            private final CharSequence[] NAME = {
                    "创建书帖", "回复帖子", "回复评论", "点赞评论"
            };
            private final int[] TYPE = {
                    DynamicCard.TYPE_CREATE_POST,
                    DynamicCard.TYPE_COMMENT,
                    DynamicCard.TYPE_COMMENT_REPLY,
                    DynamicCard.TYPE_THUMB_UP
            };

            @Override
            public Fragment getItem(int position) {
                return DynamicFragment.newInstance(mUserId, TYPE[position]);
            }

            @Override
            public int getCount() {
                return TYPE.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return NAME[position];
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {
        setTitle(R.string.title_dynamic);
    }

}
