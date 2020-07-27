package tech.minesoft.minetv.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.FocusHighlightHelper;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.OnChildViewHolderSelectedListener;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.adapter.ContentViewPagerAdapter;
import tech.minesoft.minetv.base.BaseActivity;
import tech.minesoft.minetv.fragment.OnFragmentInteractionListener;
import tech.minesoft.minetv.greendao.DaoHelper;
import tech.minesoft.minetv.presenter.TitlePresenter;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.utils.IOUtils;
import tech.minesoft.minetv.utils.JsonUtils;
import tech.minesoft.minetv.utils.SizeUtils;
import tech.minesoft.minetv.vo.Title;
import tech.minesoft.minetv.widget.ScaleTextView;

public class MineActivity extends BaseActivity implements OnFragmentInteractionListener,
        ViewTreeObserver.OnGlobalFocusChangeListener, View.OnKeyListener, View.OnClickListener {

    private static final String TAG = "MineActivity";

    private TextView mOldTitle;
    private ImageView mIvNetwork;
    private ArrayObjectAdapter mArrayObjectAdapter;

    private int mCurrentPageIndex = 0;
    private boolean isSkipTabFromViewPager = false;
    private NetworkChangeReceiver networkChangeReceiver;

    public ArrayObjectAdapter getArrayObjectAdapter() {
        return mArrayObjectAdapter;
    }

    public HorizontalGridView getHorizontalGridView() {
        return mHorizontalGridView;
    }

    private Handler mHandler = new MyHandler(this);

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.e(TAG, "onFragmentInteraction: " + uri);

        switch (uri.toString()) {
            case Const.URI_HIDE_TITLE:
                handleTitleVisible(false);
                break;
            case Const.URI_SHOW_TITLE:
                handleTitleVisible(true);
                break;
        }
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        if (newFocus == null || oldFocus == null) {
            return;
        }
        if (newFocus.getId() == R.id.tv_main_title && oldFocus.getId() == R.id.tv_main_title) {
            ((TextView) newFocus).setTextColor(getColor(R.color.colorWhite));
            ((TextView) newFocus).getPaint().setFakeBoldText(true);

            ((TextView) oldFocus).setTextColor(getColor(R.color.colorWhite));
            ((TextView) oldFocus).getPaint().setFakeBoldText(false);
        } else if (newFocus.getId() == R.id.tv_main_title && oldFocus.getId() != R.id.tv_main_title) {
            ((TextView) newFocus).setTextColor(getColor(R.color.colorWhite));
            ((TextView) newFocus).getPaint().setFakeBoldText(true);
        } else if (newFocus.getId() != R.id.tv_main_title && oldFocus.getId() == R.id.tv_main_title) {
            ((TextView) oldFocus).setTextColor(getColor(R.color.colorBlue));
            ((TextView) oldFocus).getPaint().setFakeBoldText(false);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            //            isPressBack = true;
            switch (v.getId()) {
                case R.id.btn_clear_all:
                case R.id.btn_clear_view:
                case R.id.btn_clear_star:
                case R.id.info_tips:
                    if (mHorizontalGridView != null) {
                        mHorizontalGridView.requestFocus();
                    }
                    return true;
                default:
                    break;
            }

        }

        return false;
    }

    @Override
    public void onClick(View v) {
        Log.i(Const.LOG_TAG, v.toString());
        switch (v.getId()) {
            case R.id.btn_clear_all:
                DaoHelper.clearViews();
                DaoHelper.clearHis();
                break;
            case R.id.btn_clear_view:
                DaoHelper.clearViews();
                break;
            case R.id.btn_clear_star:
                DaoHelper.clearStar();
                break;
            case R.id.info_tips:
                String[] tips = getResources().getStringArray(R.array.tips);
                int index = new Random().nextInt(tips.length);
                showText(tips[index]);
                break;
            default:
                break;
        }
    }

    private static class MyHandler extends Handler {

        private final WeakReference<MineActivity> mActivity;

        MyHandler(MineActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NotNull Message msg) {
            MineActivity activity = mActivity.get();
            if (activity != null) {
                if (msg.what == Const.MSG_MINE_TITLE) {
                    List<Title.DataBean> dataBeans = (List<Title.DataBean>) msg.obj;
                    ArrayObjectAdapter adapter = activity.getArrayObjectAdapter();
                    if (adapter != null) {
                        adapter.addAll(0, dataBeans);
                        activity.initViewPager(dataBeans);
                        HorizontalGridView horizontalGridView = activity.getHorizontalGridView();

                        if (dataBeans.size() > 0) {
                            if (horizontalGridView != null) {
                                // 判断 size 是否 大于2
                                int index = dataBeans.size() > 1 ? Const.TAB_DEFAULT_POSITION : 0;

                                horizontalGridView.setSelectedPositionSmooth(index);
                                View positionView = horizontalGridView.getChildAt(index);
                                if (positionView != null) {
                                    activity.mOldTitle = positionView.findViewById(R.id.tv_main_title);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        initView();
        initData();
        initListener();
        initBroadCast();
    }

    //    private boolean isPressUpDownLeftRightBack = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //        if (event.getAction() == KeyEvent.ACTION_DOWN) {
        //            switch (keyCode) {
        //                case KeyEvent.KEYCODE_DPAD_UP:
        //                case KeyEvent.KEYCODE_DPAD_DOWN:
        //                case KeyEvent.KEYCODE_BACK:
        //                case KeyEvent.KEYCODE_DPAD_LEFT:
        //                case KeyEvent.KEYCODE_DPAD_RIGHT:
        //                    isPressUpDownLeftRightBack = true;
        //                    break;
        //                default:
        //                    isPressUpDownLeftRightBack = false;
        //                    break;
        //            }
        //
        //        }
        //        if (mViewPagerAdapter != null) {
        //            ContentFragment contentFragment = (ContentFragment)
        //                    mViewPagerAdapter.getRegisteredFragment(mCurrentPageIndex);
        //            if (contentFragment != null && contentFragment.onKeyEvent(event)) {
        //                return true;
        //            }
        //        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        mHorizontalGridView
                .removeOnChildViewHolderSelectedListener(onChildViewHolderSelectedListener);
        getWindow().getDecorView().getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
        super.onDestroy();
        if (mThread != null) {
            mThread.interrupt();
        }
        unregisterReceiver(networkChangeReceiver);
    }

    private HorizontalGridView mHorizontalGridView;
    private ViewPager mViewPager;
    private Group mGroup;

    public Group getGroup() {
        return mGroup;
    }

    private ConstraintLayout mBtnClearAll;
    private ConstraintLayout mBtnClearView;
    private ConstraintLayout mBtnClearStar;
    private ScaleTextView mTipsTv;

    private Thread mThread = new Thread(() -> {
        String titleJson = IOUtils.readAssets(MineActivity.this, "MyTitle.json");
        //转换为对象
        Title title = JsonUtils.toBean(titleJson, Title.class);
        List<Title.DataBean> dataBeans = title.getData();
        if (dataBeans != null && dataBeans.size() > 0) {
            Message msg = Message.obtain();
            msg.what = Const.MSG_MINE_TITLE;
            msg.obj = dataBeans;
            mHandler.sendMessage(msg);
        }
    });

    private void initView() {
        mHorizontalGridView = findViewById(R.id.row_title);
        mViewPager = findViewById(R.id.tvp_content);
        mGroup = findViewById(R.id.id_group);
        mIvNetwork = findViewById(R.id.info_network);

        mBtnClearAll = findViewById(R.id.btn_clear_all);
        mBtnClearView = findViewById(R.id.btn_clear_view);
        mBtnClearStar = findViewById(R.id.btn_clear_star);
        mTipsTv = findViewById(R.id.info_tips);

        mViewPager.setOffscreenPageLimit(2);

        mArrayObjectAdapter = new ArrayObjectAdapter(new TitlePresenter());
        ItemBridgeAdapter itemBridgeAdapter = new ItemBridgeAdapter(mArrayObjectAdapter);
        mHorizontalGridView.setAdapter(itemBridgeAdapter);
        mHorizontalGridView.setHorizontalSpacing(SizeUtils.dp2px(this, 10));
        FocusHighlightHelper.setupBrowseItemFocusHighlight(itemBridgeAdapter,
                FocusHighlight.ZOOM_FACTOR_MEDIUM, false);
    }

    private void initData() {
        if (mThread != null) {
            mThread.start();
        }
    }

    private void initListener() {
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalFocusChangeListener(this);
        mHorizontalGridView.addOnChildViewHolderSelectedListener(onChildViewHolderSelectedListener);
        mBtnClearAll.setOnClickListener(this);
        mBtnClearView.setOnClickListener(this);
        mBtnClearStar.setOnClickListener(this);
        mTipsTv.setOnClickListener(this);

        mBtnClearAll.setOnKeyListener(this);
        mBtnClearView.setOnKeyListener(this);
        mBtnClearStar.setOnKeyListener(this);
        mTipsTv.setOnKeyListener(this);
    }

    private void initBroadCast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    private boolean isFirstIn = true;

    private void initViewPager(List<Title.DataBean> dataBeans) {

        ContentViewPagerAdapter viewPagerAdapter = new ContentViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.setData(dataBeans);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "onPageSelected position: " + position);
                if (isFirstIn) {
                    isFirstIn = false;
                } else {
                    isSkipTabFromViewPager = true;
                }
                if (position != mCurrentPageIndex) {
                    // mCurrentPageIndex = position;
                    mHorizontalGridView.setSelectedPosition(position);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void handleTitleVisible(boolean isShow) {
        if (isShow) {
            if (mGroup.getVisibility() != View.VISIBLE) {
                mGroup.setVisibility(View.VISIBLE);
            }
        } else {
            if (mGroup.getVisibility() != View.GONE) {
                mGroup.setVisibility(View.GONE);
            }
        }
    }

    private final OnChildViewHolderSelectedListener onChildViewHolderSelectedListener
            = new OnChildViewHolderSelectedListener() {
        @Override
        public void onChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
            super.onChildViewHolderSelected(parent, child, position, subposition);
            if (child != null && position != mCurrentPageIndex) {
                TextView currentTitle = child.itemView.findViewById(R.id.tv_main_title);
                if (isSkipTabFromViewPager) {
                    if (mOldTitle != null) {
                        mOldTitle.setTextColor(getColor(R.color.colorWhite));
                        Paint paint = mOldTitle.getPaint();
                        if (paint != null) {
                            paint.setFakeBoldText(false);
                            //viewpager切页标题不刷新，调用invalidate刷新
                            mOldTitle.invalidate();
                        }
                    }
                    currentTitle.setTextColor(getColor(R.color.colorBlue));
                    Paint paint = currentTitle.getPaint();
                    if (paint != null) {
                        paint.setFakeBoldText(true);
                        //viewpager切页标题不刷新，调用invalidate刷新
                        currentTitle.invalidate();
                    }
                }
                mOldTitle = currentTitle;
            }

            isSkipTabFromViewPager = false;
            setCurrentItemPosition(position);
        }
    };

    private void setCurrentItemPosition(int position) {
        if (mViewPager != null && position != mCurrentPageIndex) {
            mCurrentPageIndex = position;
            mViewPager.setCurrentItem(position);
        }
    }

    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                switch (networkInfo.getType()) {
                    case ConnectivityManager.TYPE_ETHERNET:
                        mIvNetwork.setImageResource(R.drawable.ethernet);
                        break;
                    case ConnectivityManager.TYPE_WIFI:
                        mIvNetwork.setImageResource(R.drawable.wifi);
                        break;
                    default:
                        break;
                }
            } else {
                mIvNetwork.setImageResource(R.drawable.no_net);
            }
        }
    }

    private Toast toast = null;

    @SuppressLint("ShowToast")
    public void showText(CharSequence text) {
        try {
            toast.getView().isShown();
            toast.setText(text);
        } catch (Exception e) {
            toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}


