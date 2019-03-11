package slasha.lanmu.business.create_post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import slasha.lanmu.R;
import slasha.lanmu.business.create_post.widget.BookInfoInputWidget;
import slasha.lanmu.business.create_post.widget.CreatorInfoInputWidget;
import slasha.lanmu.business.create_post.widget.NoScrollViewPager;

// TODO: 2019/3/11 consider whether to refactor with fragment
public class CreatePostActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, CreatePostActivity.class);
    }

    private NoScrollViewPager mViewPager;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        // handle ui style
        getWindow().setStatusBarColor(
                getResources().getColor(R.color.colorPrimaryDark)
        );
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
        setTitle(R.string.create_post_first_step);

        // find views
        List<View> mViews = new ArrayList<>();
        mViews.add(new BookInfoInputWidget(this));
        mViews.add(new CreatorInfoInputWidget(this));

        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setNoScroll(true);
        mViewPager.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return mViews.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(mViews.get(position));
                return mViews.get(position);
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == mViews.size() - 1) {
                    mButton.setText(R.string.create_now);
                    setTitle(R.string.create_post_secend_step);
                } else if (position == 0) {
                    mButton.setText(R.string.next);
                    setTitle(R.string.create_post_first_step);
                }
            }
        });


        mButton = findViewById(R.id.btn_next);
        mButton.setOnClickListener(v -> {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
        });
        mButton.setText(R.string.next);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
