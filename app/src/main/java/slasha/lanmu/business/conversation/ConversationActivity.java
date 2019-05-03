package slasha.lanmu.business.conversation;

import android.content.Context;
import android.content.Intent;

import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;

public class ConversationActivity extends SameStyleActivity {

    private ConversationFragment mConversationFragment;

    public static Intent newIntent(Context context) {
        return new Intent(context, ConversationActivity.class);
    }

    @Override
    protected void initData() {
        mConversationFragment = ConversationFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_container, mConversationFragment, "conversation")
                .commit();
        setTitle(R.string.title_conversation);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_container;
    }
}
