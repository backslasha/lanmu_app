package slasha.lanmu.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import slasha.lanmu.R;

public class SearchResultActivity extends AppCompatActivity {

    private static final String EXTRA_SEARCH_KEYWORD = "search_keyword";

    public static Intent newIntent(Context context, String searchKeyword) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra(EXTRA_SEARCH_KEYWORD, searchKeyword);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
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
