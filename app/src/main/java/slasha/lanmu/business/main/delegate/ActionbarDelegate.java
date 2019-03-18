package slasha.lanmu.business.main.delegate;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import slasha.lanmu.R;
import slasha.lanmu.business.main.MainActivity;

public class ActionbarDelegate {
    private MainActivity activity;
    private SearchView mSearchView;

    public ActionbarDelegate(MainActivity activity) {

        this.activity = activity;
    }

    public void delegate() {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        activity.getMenuInflater().inflate(R.menu.main, menu);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setIconifiedByDefault(false);

        // 指定 searchable Activity 信息, 这里就是自己
        SearchManager searchManager = (SearchManager)
                activity.getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null)
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(
                    activity.getComponentName())
            );
        return true;
    }

    public SearchView getSearchView() {
        return mSearchView;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return true;
    }
}
