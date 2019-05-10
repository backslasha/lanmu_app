package slasha.lanmu.business.post_detail.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.entity.card.BookCard;
import slasha.lanmu.utils.CommonUtils;
import slasha.lanmu.utils.FormatUtils;
import slasha.lanmu.utils.common.ToastUtils;
import yhb.chorus.common.adapter.SimpleAdapter;
import yhb.chorus.common.adapter.base.SimpleHolder;

public class BookInfoActivity extends SameStyleActivity {

    private static final String EXTRA_BOOK_INFO = "extra_book_info";
    private SimpleAdapter<String> mAdapter;

    public static Intent newIntent(Context context, BookCard bookCard) {
        Intent intent = new Intent(context, BookInfoActivity.class);
        intent.putExtra(EXTRA_BOOK_INFO, bookCard);
        return intent;
    }

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_book_info;
    }

    private BookCard mBookCard;

    @Override
    protected boolean initArgs(Bundle bundle) {
        mBookCard = (BookCard) bundle.getSerializable(EXTRA_BOOK_INFO);
        if (mBookCard == null) {
            ToastUtils.showToast("empty Book info!");
            return false;
        }
        return true;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle(mBookCard.getName());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new SimpleAdapter<String>(this) {
            @Override
            protected int layoutResId(int viewType) {
                return viewType;
            }

            @Override
            protected void bind(SimpleHolder holder, String s) {
                if (R.layout.item_cover ==
                        getItemViewType(holder.getAdapterPosition())) {
                    CommonUtils.setCover((ImageView) holder.itemView, s);
                } else {
                    holder.setText(android.R.id.text1, s);
                }
            }

            @Override
            public int getItemViewType(int position) {
                if (getEntities().get(position).startsWith("http")) {
                    return R.layout.item_cover;
                }
                return android.R.layout.simple_list_item_1;
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        String coverUrl = mBookCard.getCoverUrl();
        String name = "书名：" + mBookCard.getName();
        String author = "作者：" + mBookCard.getAuthor();
        String languish = "语言：" + mBookCard.getLanguish();
        String version = "版本：" + mBookCard.getVersion();
        String introduction = "简介：" + mBookCard.getIntroduction();
        String publisher = "出版社：" + mBookCard.getPublisher();
        String publishDate = "出版时间：" + FormatUtils.format2(mBookCard.getPublishDate());
        List<String> strings = Arrays.asList(coverUrl, name,
                author, languish, version,
                introduction, publisher, publishDate);
        mAdapter.performDataSetAdded(strings);
    }
}
