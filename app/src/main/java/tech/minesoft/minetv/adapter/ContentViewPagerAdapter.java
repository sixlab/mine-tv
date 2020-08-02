package tech.minesoft.minetv.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import tech.minesoft.minetv.fragment.AboutFragment;
import tech.minesoft.minetv.fragment.SettingFragment;
import tech.minesoft.minetv.fragment.SearchFragment;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.vo.Title;
import tech.minesoft.minetv.fragment.ContentFragment;


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
            case Const.TAB_SETTING:
                return SettingFragment.newInstance();
            case Const.TAB_ABOUT:
                return AboutFragment.newInstance();
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
