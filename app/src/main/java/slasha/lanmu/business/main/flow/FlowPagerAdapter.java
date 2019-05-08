package slasha.lanmu.business.main.flow;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import slasha.lanmu.utils.CommonUtils;

public class FlowPagerAdapter extends FragmentPagerAdapter {

    private FlowFragment.FlowType[] mBookPostFlows;

    public FlowPagerAdapter(FragmentManager fm, FlowFragment.FlowType[] flowTypes) {
        super(fm);
        mBookPostFlows = flowTypes;
    }

    @Override
    public Fragment getItem(int position) {
        return FlowFragment.newInstance(
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
