package tech.minesoft.minetv;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import tech.minesoft.minetv.layout.TVTabLayout;

/**
 * https://blog.csdn.net/Rookie_xue_IT/article/details/103735040
 */
public class MineActivity extends FragmentActivity {
    private final String TAG = getClass().getName();

    private TVTabLayout tabLayout;
    private ViewPager viewPager;

    private String[] titles = {"推荐", "分类", "我的"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//这行代码一定要在setContentView之前，不然会闪退
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_mine);

        tabLayout = findViewById(R.id.mine_tab_layout);
        viewPager = findViewById(R.id.mine_view_pager);

        initTabLayout();

        tabLayout.setSelectTabView(0);
    }

    private View getTabView(String title) {
        TextView customView = new TextView(MineActivity.this);
        customView.setPadding(15, 7, 15, 7);
        customView.setGravity(Gravity.CENTER);
        customView.setTextColor(Color.WHITE);
        customView.getPaint().setFakeBoldText(true);
        customView.setText(title);
        return customView;
    }

    private void initTabLayout() {
        for (int i = 0; i < titles.length; i++) {
            TextView tabView = (TextView) getTabView(titles[i]);
            TVTabLayout.TabView tab = tabLayout.newTabView().setCustomView(tabView);
            tabLayout.addTab(tab);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        //android.app.FragmentManager
        viewPager.setAdapter(new MineFragmentAdapter(fragmentManager));
        //绑定ViewPager
        tabLayout.setupWithViewPager(viewPager);
        //设置Tab焦点框
        // tabLayout.setTabFocusedBackground(R.drawable.lb_card_foreground);
        //设置tab间距
        tabLayout.setTabsMargin(10);
        //设置模式：选中Tab居中模式
        tabLayout.setMode(TVTabLayout.MODE_SCROLLABLE_INDICATOR_CENTER);
        //添加监听，处理TabView的UI效果
        tabLayout.addOnTabSelectedListener(new TVTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TVTabLayout.TabView view, int index) {
                view.getCustomView().setTextColor(getColor(R.color.bl_yellow));
            }

            @Override
            public void onTabUnSelected(TVTabLayout.TabView view, int index) {
                view.getCustomView().setTextColor(Color.WHITE);
            }
        });
    }
}
