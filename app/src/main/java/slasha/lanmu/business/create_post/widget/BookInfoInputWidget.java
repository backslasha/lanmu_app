package slasha.lanmu.business.create_post.widget;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;

import slasha.lanmu.R;

public class BookInfoInputWidget extends ScrollView {

    private View mFlAddCover;
    private TextInputEditText mEdtBookName;
    private TextInputEditText mEdtAuthorName;
    private TextInputEditText mEdtVersion;
    private TextInputEditText mEdtPublishHouse;
    private TextInputEditText mEdtPublishDate;
    private TextInputEditText mEdtPublisher;

    public BookInfoInputWidget(Context context) {
        this(context, null);
    }

    public BookInfoInputWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookInfoInputWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context)
                .inflate(R.layout.layout_input_book_info, this, true);

        mFlAddCover = findViewById(R.id.fl_add_cover);
        mEdtBookName = findViewById(R.id.edt_book_name);
        mEdtAuthorName = findViewById(R.id.edt_author_name);
        mEdtVersion = findViewById(R.id.edt_version);
        mEdtPublishHouse = findViewById(R.id.edt_publish_house);
        mEdtPublishDate = findViewById(R.id.edt_publish_date);
        mEdtPublisher = findViewById(R.id.edt_publisher);
    }
}
