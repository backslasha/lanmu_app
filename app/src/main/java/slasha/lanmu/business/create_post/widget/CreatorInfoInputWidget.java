package slasha.lanmu.business.create_post.widget;

import android.content.Context;
import com.google.android.material.textfield.TextInputEditText;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ScrollView;

import slasha.lanmu.R;

public class CreatorInfoInputWidget extends ScrollView {

    private TextInputEditText mEdtCreatorName;
    private TextInputEditText mEdtCreatorPhone;
    private TextInputEditText mEdtCreatorEmail;
    private TextInputEditText mEdtCreatorMessage;

    public CreatorInfoInputWidget(Context context) {
        this(context, null);
    }

    public CreatorInfoInputWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CreatorInfoInputWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context)
                .inflate(R.layout.layout_input_creator_info, this, true);

        mEdtCreatorName = findViewById(R.id.edt_creator_name);
        mEdtCreatorPhone = findViewById(R.id.edt_creator_phone);
        mEdtCreatorEmail = findViewById(R.id.edt_creator_email);
        mEdtCreatorMessage = findViewById(R.id.edt_creator_message);
    }
}
