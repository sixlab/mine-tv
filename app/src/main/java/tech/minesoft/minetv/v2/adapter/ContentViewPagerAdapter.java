package tech.minesoft.minetv.v2.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import tech.minesoft.minetv.fragment.MineFragment;
import tech.minesoft.minetv.fragment.SearchFragment;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.v2.bean.Title;
import tech.minesoft.minetv.v2.fragment.ContentFragment;


public class ContentViewPagerAdapter extends SmartFragmentStatePagerAdapter {
    private static final String TAG = "ContentViewPagerAdapter";

    private List<Title.DataBean> dataBeans;

    public ContentViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        String tabCode = dataBeans.get(position).getTabCode();
        switch (tabCode){
            case Const.TAB_SEARCH:
                return SearchFragment.newInstance();
            case Const.TAB_MINE:
                return MineFragment.newInstance();
            default:
                return ContentFragment.newInstance(position, tabCode);
        }
    }

    @Override
    public int getCount() {
        return dataBeans == null ? 0 : dataBeans.size();
    }

    public void setData(List<Title.DataBean> dataBeans) {
        this.dataBeans = dataBeans;
    }
}
