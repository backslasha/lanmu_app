package slasha.lanmu.business.create_post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.business.create_post.widget.BookInfoInputWidget;
import slasha.lanmu.business.create_post.widget.PostEditWidget;
import slasha.lanmu.entity.api.post.CreatePostModel;
import slasha.lanmu.entity.card.BookPostCard;
import slasha.lanmu.persistence.UserInfo;
import slasha.lanmu.utils.AppUtils;
import slasha.lanmu.utils.common.ToastUtils;
import slasha.lanmu.widget.NoScrollViewPager;

// TODO: 2019/3/11 consider whether to refactor with fragment
public class CreatePostActivity extends SameStyleActivity
        implements CreatePostContract.View {

    @BindView(R.id.view_pager)
    NoScrollViewPager mViewPager;

    @BindView(R.id.btn_next)
    Button mButtonNext;

    @BindView(R.id.btn_prev)
    Button mButtonPrev;

    @BindString(R.string.create_post_first_step)
    String stepOne;

    @BindString(R.string.create_post_second_step)
    String stepTwo;

    @BindString(R.string.create_now)
    String createNow;

    @BindString(R.string.next)
    String next;

    BookInfoInputWidget mBookInfoInputWidget;
    PostEditWidget mPostEditWidget;
    List<View> mViews = new ArrayList<>(2);

    private CreatePostContract.Presenter mPresenter;
    private List<ResultListener> mResultListeners = new ArrayList<>();

    public static Intent newIntent(Context context) {
        return new Intent(context, CreatePostActivity.class);
    }

    public void addResultListener(ResultListener resultListener) {
        mResultListeners.add(resultListener);
    }

    public void removeResultListener(ResultListener resultListener) {
        mResultListeners.remove(resultListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(stepOne);
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mViews.add(mBookInfoInputWidget = new BookInfoInputWidget(this));
        mViews.add(mPostEditWidget = new PostEditWidget(this));

        addResultListener(mBookInfoInputWidget);
        addResultListener(mPostEditWidget);

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
            public void destroyItem(@NonNull ViewGroup container, int position,
                                    @NonNull Object object) {
                container.removeView((View) object);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == mViews.size() - 1) {
                    mButtonNext.setText(createNow);
                    mButtonPrev.setVisibility(View.VISIBLE);
                    setTitle(stepTwo);
                } else if (position == 0) {
                    mButtonPrev.setVisibility(View.GONE);
                    mButtonNext.setText(next);
                    setTitle(stepOne);
                }
            }
        });

        mButtonNext.setText(next);
        mButtonPrev.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_next)
    void next() {
        int currentItem = mViewPager.getCurrentItem();
        if (currentItem == mViews.size() - 1) {
            if (mPostEditWidget.withEnoughText()) {
                myPresenter().performCreate(collectInfo());
            } else {
                mPostEditWidget.showTextTooShortTip();
            }
        } else {
            if (mBookInfoInputWidget.allFilled()) {
                if (mBookInfoInputWidget.withCover()) {
                    mViewPager.setCurrentItem(currentItem + 1, true);
                } else {
                    mBookInfoInputWidget.showCoverNeededTip();
                }
            } else {
                mBookInfoInputWidget.showFieldsNeededTip();
            }
        }
    }

    private CreatePostModel collectInfo() {
        return new CreatePostModel(
                mBookInfoInputWidget.getBookInfo(),
                UserInfo.id(),
                mPostEditWidget.getText(),
                mPostEditWidget.getImages()
        );
    }

    @OnClick(R.id.btn_prev)
    void previous() {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
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

    @Override
    public void showCreateSuccess(BookPostCard card) {
        ToastUtils.showToast(R.string.publish_succeed);
        AppUtils.jumpToMainPage(this);
        finish();
    }

    @Override
    public void showActionFail(String info) {
        ToastUtils.showToast(getString(R.string.publish_failed) + "：" + info);
    }

    @Override
    public CreatePostContract.Presenter myPresenter() {
        if (mPresenter == null) {
            mPresenter = new CreatePostPresenterImpl(this, this);
        }
        return mPresenter;
    }

    @Override
    public void showLoadingIndicator() {
        showProgressDialog();
    }

    @Override
    public void hideLoadingIndicator() {
        hideProgressDialog();
    }

    public interface ResultListener {
        void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);
    }
}
