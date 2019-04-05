package slasha.lanmu.business.post_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.application.LanmuApplication;
import slasha.lanmu.bean.Book;
import slasha.lanmu.bean.BookPost;
import slasha.lanmu.bean.Comment;
import slasha.lanmu.bean.User;
import slasha.lanmu.business.profile.UserProfileActivity;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.ToastUtils;
import slasha.lanmu.widget.AppBarStateChangeListener;
import slasha.lanmu.widget.StickyHeaderItemDecoration;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class PostDetailActivity extends SameStyleActivity implements PostDetailContract.PostDetailView {


    private static final CharSequence EMPTY_TITLE = " ";
    private static final String EXTRA_BOOK_POST = "extra_book_post";
    private RecyclerView mRecyclerView;
    private TextView mTvTitle, mTvAuthor, mTvDescription;
    private ImageView mIvCover;
    private SimpleAdapter<Comment> mAdapter;
    private BookPost mBookPost;

    public static Intent newIntent(Context context, BookPost bookPost) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(EXTRA_BOOK_POST, bookPost);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // book info
        mIvCover = findViewById(R.id.iv_cover);
        mTvTitle = findViewById(R.id.tv_title);
        mTvAuthor = findViewById(R.id.tv_author_name);
        mTvDescription = findViewById(R.id.tv_description);

        CollapsingToolbarLayout collapsingToolbarLayout =
                findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(EMPTY_TITLE);// "" 将 显示 app label

        AppBarLayout appbarLayout = findViewById(R.id.app_bar_layout);
        appbarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            protected void onCollapsed(AppBarLayout appBarLayout) {
                collapsingToolbarLayout.setTitle(mBookPost.getBook().getName());
            }

            @Override
            protected void onScrolled(AppBarLayout appBarLayout) {
                collapsingToolbarLayout.setTitle(EMPTY_TITLE);
            }
        });

        // creator info


        // comments
        mRecyclerView = findViewById(R.id.recycler_view);

        handleIntent(getIntent());

    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        mBookPost = (BookPost) intent.getSerializableExtra(EXTRA_BOOK_POST);
        showDetail(mBookPost);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_post_detail;
    }

    @Override
    public void showDetail(BookPost bookPost) {
        showBookInfo(bookPost.getBook());
        showComments(bookPost.getComments());
        // TODO: 2019/3/12 add creator info
        // showCreatorInfo(mBookPost.getCreateInfo());
    }

    private void showBookInfo(Book book) {
        Picasso.with(LanmuApplication.instance())
                .load(book.getCoverUrl())
                .into(mIvCover);
        mTvTitle.setText(book.getName());
        mTvAuthor.setText(book.getAuthor());
        mTvDescription.setText("现代人内心流失的东西，这家杂货店能帮你找回——僻静的街道旁有一家杂货店，只要写下烦恼投进卷帘门的投信口，第二天就会在店后的牛奶箱里得到回答。因男友身患绝症，年轻女孩静子在爱情与梦想间徘徊；克郎为了音乐梦想离家漂泊，却在现实中寸步难行；少年浩介面临家庭巨变，挣扎在亲情与未来的迷茫中……他们将困惑写成信投进杂货店，随即奇妙的事情竟不断发生。生命中的一次偶然交会，将如何演绎出截然不同的人生？如今回顾写作过程，我发现自己始终在思考一个问题：站在人生的岔路口，人究竟应该怎么做？我希望读者能在掩卷时喃喃自语：我从未读过这样的小说。——东野圭吾");
        // TODO: 2019/3/12 complete book info
    }

    private void showComments(List<Comment> comments) {
        if (CommonUtils.isEmpty(comments)) {
            ToastUtils.showToast("no comments found!");
        } else {
            if (mAdapter == null) {
                mAdapter = new SimpleAdapter<Comment>(
                        PostDetailActivity.this) {
                    @Override
                    protected int layoutResId(int viewType) {
                        return R.layout.item_comment;
                    }

                    @Override
                    public void bind(SimpleHolder holder, Comment comment) {
                        Picasso.with(LanmuApplication.instance())
                                .load(comment.getFrom().getAvatarUrl())
                                .into((ImageView) holder.getView(R.id.iv_avatar));

                        holder.setText(R.id.tv_username, comment.getFrom().getUsername());
                        holder.setText(R.id.tv_comment_content, comment.getContent());
                        holder.setText(R.id.tv_publish_date, "2007年7月24日");
                        holder.setText(R.id.tv_thumb_up_count, "999");

                        View.OnClickListener listener =
                                v -> jumpToUserProfile(comment.getFrom());
                        holder.getView(R.id.tv_username).setOnClickListener(listener);
                        holder.getView(R.id.iv_avatar).setOnClickListener(listener);

                        // TODO: 2019/3/12 complete data structures
                    }

                };
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mRecyclerView.addItemDecoration(
                        new StickyHeaderItemDecoration.Builder(
                                new StickyHeaderItemDecoration.HeaderHelper() {
                                    @Override
                                    public boolean isGroupCaptain(int position) {
                                        return position == 0;
                                    }

                                    @Override
                                    public String getGroupTitle(int position) {
                                        return "评论列表";
                                    }

                                    @Override
                                    public int getItemCount() {
                                        return comments.size();
                                    }
                                }
                        ).setHeaderTextColor(getResources().getColor(R.color.colorPrimary))
                                .build(this)
                );


            }
            mAdapter.performDataSetChanged(comments);
        }
    }

    private void jumpToUserProfile(User user) {
        startActivity(
                UserProfileActivity.newIntent(this, user)
        );
    }

    @Override
    public PostDetailContract.PostDetailPresenter myPresenter() {
        return null;
    }

    @Override
    public void showLoadingIndicator() {
        showProgressDialog();
    }

    @Override
    public void hideLoadingIndicator() {
        hideProgressDialog();
    }
}
