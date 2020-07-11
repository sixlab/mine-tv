package tech.minesoft.minetv.layout;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class TVTabLayout extends HorizontalScrollView {
    private static final String TAG = TVTabLayout.class.getSimpleName();

    //所有的TabView
    private final ArrayList<TabView> mTabs = new ArrayList<>();
    //当前选中的TabView
    private TabView mSelectedTabView = null;
    //当前选中的Tab index
    private int mSelectedIndex = -1;

    //tab之间的间距
    private int tabsMargin = 0;
    //是否循环，焦点移动边界时，是否循环
    private boolean isLoop = false;

    private LinearLayout tabViewLinearLayout;
    /**
     * Tab填充满显示区域，不可滚动
     */
    public static final int MODE_FIXED_NON_SCROLLABLE = 1;
    /**
     * Tab可滚动且选中栏目一直居中
     */
    public static final int MODE_SCROLLABLE_INDICATOR_CENTER = 2;
    /**
     * Tab可滚动
     */
    public static final int MODE_SCROLLABLE = 3;

    @IntDef(value = {MODE_FIXED_NON_SCROLLABLE, MODE_SCROLLABLE, MODE_SCROLLABLE_INDICATOR_CENTER})
    public @interface Mode {
    }

    //TabView Mode
    private int mMode = MODE_FIXED_NON_SCROLLABLE;

    //Tab焦点背景
    private Drawable mTabFocusedBackground = null;
    //TabView的选中监听，观察者模式
    private ArrayList<OnTabSelectedListener> mSelectedListeners = new ArrayList<>();
    //绑定的ViewPager
    private ViewPager viewPager;
    //ViewPager页面切换监听
    private ViewPager.OnPageChangeListener onPageChangeListener;
    //TabLayout切换监听
    private OnTabSelectedListener onTabSelectedListener;

    public TVTabLayout(Context context) {
        this(context, null);
    }

    public TVTabLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TVTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //        setOrientation(HORIZONTAL);
        setFocusable(true);
        setFocusableInTouchMode(true);
        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        setFillViewport(true);

        // Add the TabStrip
        tabViewLinearLayout = new LinearLayout(context);
        tabViewLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        tabViewLinearLayout.setFocusable(false);
        tabViewLinearLayout.setFocusableInTouchMode(false);

        applyMode();
        addView(tabViewLinearLayout, new HorizontalScrollView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    private void applyMode() {
        switch (mMode) {
            case MODE_FIXED_NON_SCROLLABLE:
                tabViewLinearLayout.setGravity(Gravity.CENTER);
                break;
            case MODE_SCROLLABLE:
            case MODE_SCROLLABLE_INDICATOR_CENTER:
                tabViewLinearLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
                break;
        }
    }

    public void setMode(@Mode int mode) {
        if (mode != mMode) {
            this.mMode = mode;

            applyMode();
        }
    }

    /**
     * Return the current mode
     *
     * @see #setMode(int)
     */
    @Mode
    public int getMode() {
        return mMode;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getChildCount() == 1) {
            View view = getChildAt(0);
            int childViewWidth = view.getMeasuredWidth();

            switch (mMode) {
                case MODE_FIXED_NON_SCROLLABLE:
                    if (childViewWidth >= getMeasuredWidth()) {
                        //更新Tab的尺寸
                        int tabViewCount = tabViewLinearLayout.getChildCount();
                        for (int i = 0; i < tabViewCount; i++) {
                            ViewGroup.LayoutParams lp = tabViewLinearLayout.getChildAt(i).getLayoutParams();
                            lp.width = getMeasuredWidth() / tabViewCount;
                        }
                    }
                    break;
                //其他模式的修改逻辑
            }
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            if (mSelectedIndex >= 0) {
                setSelectTabView(mSelectedIndex);
            } else {
                setSelectTabView(0);
            }
        } else {
            setUnFocusedTabView(mSelectedIndex);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() != KeyEvent.ACTION_DOWN) {
            return super.dispatchKeyEvent(event);
        }

        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                int currentTabIndexLeft = mSelectedIndex;
                if (isLoop) {
                    int nextLeftPosition = currentTabIndexLeft > 0 ? --currentTabIndexLeft : getTabCount() - 1;
                    setSelectTabView(nextLeftPosition);
                } else {
                    if (currentTabIndexLeft > 0) {
                        int nextLeftPosition = --currentTabIndexLeft;
                        setSelectTabView(nextLeftPosition);
                    } else {
                        Log.d(TAG, "index == 0, 不循环");
                    }
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                int currentTabIndexRight = mSelectedIndex;
                if (isLoop) {
                    int nextRightPosition = currentTabIndexRight < getTabCount() - 1 ? ++currentTabIndexRight : 0;
                    setSelectTabView(nextRightPosition);
                } else {
                    if (currentTabIndexRight < getTabCount() - 1) {
                        int nextRightPosition = ++currentTabIndexRight;
                        setSelectTabView(nextRightPosition);
                    } else {
                        Log.d(TAG, "index == count, 不循环");
                    }
                }
                return true;
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public void setOnKeyListener(OnKeyListener l) {
        super.setOnKeyListener(l);
    }

    /**
     * 设置Tab间距
     *
     * @param margin 间距
     */
    public void setTabsMargin(int margin) {
        this.tabsMargin = margin;
    }

    /**
     * 添加tab
     *
     * @param view tabView
     */
    public void addTab(@NonNull TabView view) {
        addTab(view, tabsMargin);
    }

    /**
     * 添加tab
     *
     * @param view tabView
     */
    public void addTab(@NonNull TabView view, int tabsMargin) {
        view.setFocusable(false);
        view.setFocusableInTouchMode(false);
        mTabs.add(view);
        tabViewLinearLayout.addView(view, createLayoutParamsForTabs());

        if (mSelectedIndex == mTabs.indexOf(view)) {
            setSelectTabView(mSelectedIndex);
        }
    }

    private LinearLayout.LayoutParams createLayoutParamsForTabs() {
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        //设置Tab间距
        lp.leftMargin = tabsMargin;
        updateTabViewLayoutParams(lp);
        return lp;
    }

    private void updateTabViewLayoutParams(LinearLayout.LayoutParams lp) {
        //根据模式设置Tab尺寸
        if (mMode == MODE_FIXED_NON_SCROLLABLE) {
            lp.width = 0;
            lp.weight = 1;
        } else {
            lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            lp.weight = 0;
        }
    }

    /**
     * 获取当前选中的Tab index
     *
     * @return select index
     */
    public int getSelectedTabPosition() {
        return mSelectedIndex;
    }

    /**
     * 根据Index 获取TabView
     *
     * @param index index
     * @return tabView
     */
    public TabView getTab(int index) {
        return (index < 0 || index >= getTabCount()) ? null : mTabs.get(index);
    }

    /**
     * 获取TabView个数
     *
     * @return TabView个数
     */
    public int getTabCount() {
        return mTabs.size();
    }

    /**
     * 设置获取焦点的TavView
     *
     * @param position
     */
    protected void setFocusedTabView(int position) {
        final int tabCount = getTabCount();
        if (position < tabCount) {
            if (mTabFocusedBackground != null && getTab(position) != null) {
                getTab(position).setBackground(mTabFocusedBackground);
            }
        }
    }

    /**
     * 设置失去焦点的TabView
     *
     * @param position
     */
    protected void setUnFocusedTabView(int position) {
        final int tabCount = getTabCount();
        if (position < tabCount) {
            if (mTabFocusedBackground != null && getTab(position) != null) {
                getTab(position).setBackground(null);
            }
        }
    }

    /**
     * 设置Tab选中状态
     *
     * @param position
     */
    public void setSelectTabView(int position) throws IndexOutOfBoundsException {
        final int tabCount = getTabCount();
        if (position < tabCount) {
            setSelectTabView(getTab(position), position);
        } else {
            throw new IndexOutOfBoundsException("index out of TabView count");
        }
    }

    /**
     * 设置Tab选中状态
     *
     * @param tabView
     */
    void setSelectTabView(TabView tabView, int position) {
        final TabView currentTab = mSelectedTabView;
        final int currentTabIndex = mSelectedIndex;

        if (currentTab == tabView) {
            if (currentTab != null) {
                dispatchTabSelected(currentTab, currentTabIndex);

                if (hasFocus()) {
                    setFocusedTabView(currentTabIndex);
                }
            }
        } else {
            if (currentTab != null) {
                dispatchTabUnselected(currentTab, currentTabIndex);
                setUnFocusedTabView(currentTabIndex);
            }
            mSelectedTabView = tabView;
            mSelectedIndex = position;
            if (tabView != null) {
                dispatchTabSelected(tabView, position);

                if (hasFocus()) {
                    setFocusedTabView(position);
                }
            }

            setScrollPosition();
        }
    }

    /**
     * 分发Tab选中状态
     *
     * @param tab      selected TabView
     * @param position selected TabView`s index
     */
    private void dispatchTabSelected(@NonNull TabView tab, int position) {
        for (int i = mSelectedListeners.size() - 1; i >= 0; i--) {
            mSelectedListeners.get(i).onTabSelected(tab, position);
        }
    }

    private void dispatchTabUnselected(@NonNull TabView tab, int position) {
        for (int i = mSelectedListeners.size() - 1; i >= 0; i--) {
            mSelectedListeners.get(i).onTabUnSelected(tab, position);
        }
    }

    public void setTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        if (onTabSelectedListener != null) {
            addOnTabSelectedListener(onTabSelectedListener);
        }
    }

    public void addOnTabSelectedListener(@NonNull OnTabSelectedListener listener) {
        if (!mSelectedListeners.contains(listener)) {
            mSelectedListeners.add(listener);
        }
    }

    public void removeOnTabSelectedListener(@NonNull OnTabSelectedListener listener) {
        if (mSelectedListeners != null) {
            mSelectedListeners.remove(listener);
        }
    }

    public void setTabFocusedBackground(Drawable drawable) {
        mTabFocusedBackground = drawable;
    }

    public void setTabFocusedBackground(int resource) {
        Drawable drawable = getResources().getDrawable(resource);
        mTabFocusedBackground = drawable;
    }

    /**
     * 滚动处理
     */
    private void setScrollPosition() {
        View tabView = mSelectedTabView;
        int index = mSelectedIndex;
        if (mMode == MODE_SCROLLABLE || mMode == MODE_SCROLLABLE_INDICATOR_CENTER) {
            int scrollViewWidth = getMeasuredWidth();
            int scrolled = getScrollX();
            int tabViewX = (int) tabView.getX();
            int tabViewWidth = tabView.getMeasuredWidth();
            int tabViewLinearLayoutWidth = tabViewLinearLayout.getMeasuredWidth();
            Log.d(TAG, "index = " + index + " --- scrollViewWidth = " + scrollViewWidth + " --- scrolled = " + scrolled + " --- tabViewX = " + tabViewX + " --- tabViewWidth" + tabViewWidth);

            switch (mMode) {
                case MODE_SCROLLABLE:
                    if (tabViewX + tabViewWidth > scrollViewWidth + scrolled) {
                        int scrollTo = tabViewX + tabViewWidth - scrollViewWidth;
                        smoothScrollTo(scrollTo, 0);
                    } else if (scrolled > tabViewX) {
                        int scrollTo = tabViewX;
                        smoothScrollTo(scrollTo, 0);
                    }
                    break;
                case MODE_SCROLLABLE_INDICATOR_CENTER:
                    if (tabViewX + tabViewWidth / 2 < scrollViewWidth / 2 ||
                            tabViewX + tabViewWidth / 2 > tabViewLinearLayoutWidth - scrollViewWidth / 2) {
                        break;
                    }

                    if ((tabViewX + tabViewWidth / 2) != scrolled + scrollViewWidth / 2) {
                        int scrollTo = tabViewX + tabViewWidth / 2 - scrollViewWidth / 2;
                        smoothScrollTo(scrollTo, 0);
                    }
                    break;
            }
        }
    }

    @NonNull
    public TabView newTabView() {
        TabView tabView = new TabView(getContext());
        tabView.setFocusable(false);
        return tabView;
    }

    /**
     * 绑定ViewPager
     *
     * @param viewPager ViewPager
     */
    public void setupWithViewPager(ViewPager viewPager) {
        if (this.viewPager != null) {
            //移除所有监听
            if (onPageChangeListener != null) {
                this.viewPager.removeOnPageChangeListener(onPageChangeListener);
            }

            if (onTabSelectedListener != null) {
                this.removeOnTabSelectedListener(onTabSelectedListener);
            }
        }

        if (viewPager != null) {
            this.viewPager = viewPager;
            //绑定，设置监听
            onPageChangeListener = new TabLayoutOnPageChangeListener(this);
            viewPager.addOnPageChangeListener(onPageChangeListener);

            onTabSelectedListener = new ViewPagerOnTabSelectedListener(viewPager);
            this.addOnTabSelectedListener(onTabSelectedListener);
        } else {
            this.viewPager = null;
            this.onPageChangeListener = null;
            this.onTabSelectedListener = null;
        }
    }

    /**
     * public interface OnTabFocusListener{
     * public void onTabFocused(View view, int index);
     * <p>
     * public void onTabUnFocus(View view, int index);
     * }
     **/


    public static final class TabView extends FrameLayout {
        private TextView mCustomView;

        public TabView(@NonNull Context context) {
            super(context);
        }

        public TabView(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public TabView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Nullable
        public TextView getCustomView() {
            return mCustomView;
        }

        @NonNull
        public TabView setCustomView(@Nullable TextView view) {
            mCustomView = view;
            updateView();
            return this;
        }

        void updateView() {
            if (mCustomView != null) {
                LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                setPadding(4, 4, 4, 4);
                addView(mCustomView, layoutParams);
            }
        }
    }

    public static class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final WeakReference<TVTabLayout> mTabLayoutRef;
        private int mPreviousScrollState;
        private int mScrollState;

        public TabLayoutOnPageChangeListener(TVTabLayout tabLayout) {
            mTabLayoutRef = new WeakReference<>(tabLayout);
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            mPreviousScrollState = mScrollState;
            mScrollState = state;
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset,
                                   final int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(final int position) {
            //监听ViewPager的Item选中事件，切换Tab
            final TVTabLayout tabLayout = mTabLayoutRef.get();
            if (tabLayout != null && tabLayout.getSelectedTabPosition() != position
                    && position < tabLayout.getTabCount()) {
                // Select the tab, only updating the indicator if we're not being dragged/settled
                // (since onPageScrolled will handle that).
                tabLayout.setSelectTabView(position);
            }
        }
    }

    public interface OnTabSelectedListener {
        void onTabSelected(TabView view, int index);

        void onTabUnSelected(TabView view, int index);
    }

    public static class ViewPagerOnTabSelectedListener implements OnTabSelectedListener {
        private final ViewPager mViewPager;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager) {
            mViewPager = viewPager;
        }

        @Override
        public void onTabSelected(TabView view, int index) {
            //监听Tab选中事件，切换ViewPager
            if (mViewPager != null && mViewPager.getCurrentItem() != index) {
                try {
                    //部分型号上抛出异常：
                    //IllegalStateException: FragmentManager is already executing transactions
                    //简单捕获处理下，后续完善
                    mViewPager.setCurrentItem(index);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onTabUnSelected(TabView view, int index) {

        }
    }
}
