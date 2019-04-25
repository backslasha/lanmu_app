package slasha.lanmu.business.create_post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.business.create_post.widget.BookInfoInputWidget;
import slasha.lanmu.widget.NoScrollViewPager;
import slasha.lanmu.business.create_post.widget.PostEditWidget;

// TODO: 2019/3/11 consider whether to refactor with fragment
public class CreatePostActivity extends SameStyleActivity {

    private NoScrollViewPager mViewPager;
    private Button mButtonNext, mButtonPrev;
    private BookInfoInputWidget mBookInfoInputWidget;
    private PostEditWidget mPostEditWidget;

    public interface ResultListener {
        void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);
    }

    private List<ResultListener> mResultListeners = new ArrayList<>();

    public void addResultListener(ResultListener resultListener) {
        mResultListeners.add(resultListener);
    }

    public void removeResultListener(ResultListener resultListener) {
        mResultListeners.remove(resultListener);
    }


    public static Intent newIntent(Context context) {
        return new Intent(context, CreatePostActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.create_post_first_step);

        // find views
        List<View> mViews = new ArrayList<>();
        mBookInfoInputWidget = new BookInfoInputWidget(this);
        mPostEditWidget = new PostEditWidget(this);
        addResultListener(mBookInfoInputWidget);
        addResultListener(mPostEditWidget);

        mViews.add(mBookInfoInputWidget);
        mViews.add(mPostEditWidget);


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
                    mButtonNext.setText(R.string.create_now);
                    mButtonPrev.setVisibility(View.VISIBLE);
                    setTitle(R.string.create_post_second_step);
                } else if (position == 0) {
                    mButtonPrev.setVisibility(View.GONE);
                    mButtonNext.setText(R.string.next);
                    setTitle(R.string.create_post_first_step);
                }
            }
        });


        mButtonNext = findViewById(R.id.btn_next);
        mButtonPrev = findViewById(R.id.btn_prev);
        mButtonNext.setOnClickListener(v -> {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
        });
        mButtonPrev.setOnClickListener(v -> {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
        });
        mButtonNext.setText(R.string.next);
        mButtonPrev.setVisibility(View.GONE);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_create_post;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (ResultListener resultListener : mResultListeners) {
            resultListener.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (mButtonPrev.getVisibility() == View.VISIBLE) {
            mButtonPrev.performClick();
        } else {
            super.onBackPressed();
        }
    }
}
