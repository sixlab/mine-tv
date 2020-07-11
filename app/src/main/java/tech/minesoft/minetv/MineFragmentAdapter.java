package tech.minesoft.minetv;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MineFragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();

    public MineFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);

        fragments.add(new MineFragment());
        fragments.add(new MineFragment1());
        fragments.add(new MineFragment2());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
