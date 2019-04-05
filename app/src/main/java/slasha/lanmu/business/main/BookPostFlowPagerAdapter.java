package slasha.lanmu.business.main;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import slasha.lanmu.utils.CommonUtils;

public class BookPostFlowPagerAdapter extends FragmentPagerAdapter {

    private BookPostFlowFragment.FlowType[] mBookPostFlows;

    BookPostFlowPagerAdapter(FragmentManager fm, BookPostFlowFragment.FlowType[] flowTypes) {
        super(fm);
        mBookPostFlows = flowTypes;
    }

    @Override
    public Fragment getItem(int position) {
        return BookPostFlowFragment.newInstance(
                CommonUtils.isEmpty(mBookPostFlows) ? null : mBookPostFlows[position]
        );
    }

    @Override
    public int getCount() {
        return CommonUtils.isEmpty(mBookPostFlows) ? 0 : mBookPostFlows.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mBookPostFlows[position].getName();
    }
}
