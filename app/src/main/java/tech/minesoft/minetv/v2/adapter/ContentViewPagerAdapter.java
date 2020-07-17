package tech.minesoft.minetv.v2.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import tech.minesoft.minetv.v2.bean.Title;
import tech.minesoft.minetv.v2.fragment.ContentFragment;
import tech.minesoft.minetv.fragment.MineFragment;
import tech.minesoft.minetv.fragment.SearchFragment;


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
            case "TabSearch":
                return SearchFragment.newInstance();
            case "TabMine":
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
