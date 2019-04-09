package slasha.lanmu.business.post_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

public class PostDetailActivity extends SameStyleActivity
        implements PostDetailContract.PostDetailView {


    private static final CharSequence EMPTY_TITLE = " ";
    private static final String EXTRA_BOOK_POST = "extra_book_post";
    private RecyclerView mRecyclerView;
    private TextView mTvTitle, mTvDescription, mTvPostContent;
    private ImageView mIvCover;
    private SimpleAdapter<Comment> mAdapter;
    private BookPost mBookPost;
    private PostDetailContract.PostDetailPresenter mPostDetailPresenter;
    private ImageView mIvAvatar;
    private TextView mTvCreatorName;

    public static Intent newIntent(Context context, BookPost bookPost) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(EXTRA_BOOK_POST, bookPost);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIvAvatar = findViewById(R.id.iv_avatar);
        mTvCreatorName = findViewById(R.id.tv_username);

        // book info
        mIvCover = findViewById(R.id.iv_cover);
        mTvTitle = findViewById(R.id.tv_title);
        mTvDescription = findViewById(R.id.tv_description);
        mTvPostContent = findViewById(R.id.tv_post_content);

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

        myPresenter().performPullComments(mBookPost.getId());
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_post_detail;
    }

    @Override
    public void showDetail(BookPost bookPost) {

        if (bookPost == null) {
            return;
        }

        mTvPostContent.setText(bookPost.getContent());

        Book book = bookPost.getBook();
        if (book != null) {
            Picasso.with(LanmuApplication.instance())
                    .load(book.getImages())
                    .into(mIvCover);
            mTvTitle.setText(book.getName());
            mTvDescription.setText(
                    String.format("%s / %s / %s",
                            book.getAuthor(),
                            book.getPublisher(),
                            book.getPublishDate())
            );
        }

        User creator = bookPost.getCreator();
        if (creator != null) {
            Picasso.with(LanmuApplication.instance())
                    .load(creator.getAvatarUrl())
                    .into(mIvAvatar);
            mTvCreatorName.setText(creator.getUsername());

        }
        // TODO: 2019/3/12 complete book info
    }


    @Override
    public void showComments(List<Comment> comments) {
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
                        holder.setImage(R.id.iv_avatar, comment.getFrom().getAvatarUrl());
                        holder.setText(R.id.tv_username, comment.getFrom().getUsername());
                        holder.setText(R.id.tv_comment_content, comment.getContent());
                        holder.setText(R.id.tv_publish_date, "2007年7月24日");
                        holder.setText(R.id.tv_thumb_up_count, "999");
                        View.OnClickListener listener =
                                v -> jumpToUserProfile(comment.getFrom());
                        holder.getView(R.id.tv_username).setOnClickListener(listener);
                        holder.getView(R.id.iv_avatar).setOnClickListener(listener);

                        RecyclerView recyclerView
                                = (RecyclerView) holder.getView(R.id.recycler_view_replies);
                        recyclerView.setLayoutManager(
                                new LinearLayoutManager(PostDetailActivity.this)
                        );
                        CommentReplyAdapter commentReplyAdapter = new CommentReplyAdapter(
                                PostDetailActivity.this,
                                comment,
                                2,
                                true
                        );
                        commentReplyAdapter.setOnItemClickListener(
                                (CommentReplyAdapter.onItemClickListener) isExpandableItem -> {
                                    if (isExpandableItem) {
                                        ToastUtils.showToast("jump to reply board!");
                                    }else {
                                        ToastUtils.showToast("raise the reply edit view!");
                                    }
                        });
                        recyclerView.setAdapter(commentReplyAdapter);
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
                                .setPadding(0, 0, 0, 6)
                                .setHeaderTextPaddingStart(12)
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
        if (mPostDetailPresenter == null) {
            mPostDetailPresenter = new PostDetailPresenterImpl(
                    this,
                    new PostDetailModelImpl()
            );
        }
        return mPostDetailPresenter;
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
