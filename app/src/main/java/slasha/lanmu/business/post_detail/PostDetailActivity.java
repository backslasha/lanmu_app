package slasha.lanmu.business.post_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.bean.BookPost;

public class PostDetailActivity extends SameStyleActivity {


    private static final String EXTRA_BOOK_POST = "extra_book_post";

    public static Intent newIntent(Context context, BookPost bookPost) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(EXTRA_BOOK_POST, bookPost);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_post_detail;
    }
}
