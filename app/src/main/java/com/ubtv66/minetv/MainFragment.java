package com.ubtv66.minetv;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.ubtv66.minetv.data.DbHelper;
import com.ubtv66.minetv.data.SampleData;
import com.ubtv66.minetv.page.CardPresenter;
import com.ubtv66.minetv.page.detail.VodDetailActivity;
import com.ubtv66.minetv.page.search.MineSearchActivity;
import com.ubtv66.minetv.vo.VodInfo;

import java.util.List;

public class MainFragment extends BrowseFragment {
    private static final String TAG = "MainFragment";

    // private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int GRID_ITEM_WIDTH = 200; // 200
    private static final int GRID_ITEM_HEIGHT = 300; // 200

    // private final Handler mHandler = new Handler();
    // private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    // private Timer mBackgroundTimer;
    // private String mBackgroundUri;
    private BackgroundManager mBackgroundManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        DbHelper.init(getContext());

        // SampleData.initSampleData(getContext());

        prepareBackgroundManager();

        setupUIElements();

        loadRows();

        setupEventListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // if (null != mBackgroundTimer) {
        //     Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
        //     mBackgroundTimer.cancel();
        // }
    }

    private void loadRows() {
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        CardPresenter cardPresenter = new CardPresenter();
        // 第一行：记录
        {
            List<VodInfo> vodInfoList = DbHelper.loadHis(getContext());

            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
            for (VodInfo info : vodInfoList) {
                listRowAdapter.add(info);
            }
            HeaderItem header = new HeaderItem(0, getString(R.string.type_his));
            rowsAdapter.add(new ListRow(header, listRowAdapter));
        }

        // 第二行：收藏
        {
            List<VodInfo> vodInfoList = DbHelper.loadStar(getContext());

            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
            for (VodInfo info : vodInfoList) {
                listRowAdapter.add(info);
            }
            HeaderItem header = new HeaderItem(1, getString(R.string.type_star));
            rowsAdapter.add(new ListRow(header, listRowAdapter));
        }

        // 设置
        HeaderItem gridHeader = new HeaderItem(2, getString(R.string.action_settings));

        GridItemPresenter mGridPresenter = new GridItemPresenter();
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
        gridRowAdapter.add(getResources().getString(R.string.clear_his));
        gridRowAdapter.add(getString(R.string.clear_star));
        gridRowAdapter.add(getString(R.string.clear_all));
        rowsAdapter.add(new ListRow(gridHeader, gridRowAdapter));

        setAdapter(rowsAdapter);
    }

    private void prepareBackgroundManager() {
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());

        // mDefaultBackground = ContextCompat.getDrawable(getContext(), R.drawable.default_background);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void setupUIElements() {
        // setBadgeDrawable(getActivity().getResources().getDrawable(
        // R.drawable.videos_by_google_banner));
        setTitle(getString(R.string.browse_title)); // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(ContextCompat.getColor(getContext(), R.color.fastlane_background));
        // set search icon color
        setSearchAffordanceColor(ContextCompat.getColor(getContext(), R.color.search_opaque));
    }

    private void setupEventListeners() {
        setOnSearchClickedListener(view -> {
            Intent intent = new Intent(getActivity(), MineSearchActivity.class);

            getActivity().startActivity(intent);
        });

        setOnItemViewClickedListener(new ItemViewClickedListener());

        // setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    // private void updateBackground(String uri) {
    //     int width = mMetrics.widthPixels;
    //     int height = mMetrics.heightPixels;
    //     Glide.with(getActivity())
    //             .load(uri)
    //             .centerCrop()
    //             .error(mDefaultBackground)
    //             .into(new SimpleTarget<GlideDrawable>(width, height) {
    //                 @Override
    //                 public void onResourceReady(GlideDrawable resource,
    //                                             GlideAnimation<? super GlideDrawable>
    //                                                     glideAnimation) {
    //                     mBackgroundManager.setDrawable(resource);
    //                 }
    //             });
    //     mBackgroundTimer.cancel();
    // }
    //
    // private void startBackgroundTimer() {
    //     if (null != mBackgroundTimer) {
    //         mBackgroundTimer.cancel();
    //     }
    //     mBackgroundTimer = new Timer();
    //     mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    // }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof VodInfo) {
                // 小方块点击
                VodInfo info = (VodInfo) item;
                Log.d(TAG, "Item: " + item.toString());
                Intent intent = new Intent(getActivity(), VodDetailActivity.class);
                intent.putExtra(VodDetailActivity.MOVIE, info);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        VodDetailActivity.SHARED_ELEMENT_NAME)
                        .toBundle();

                getActivity().startActivity(intent, bundle);
            } else if (item instanceof String) {
                DbHelper.clearStar(getContext());
                if (((String) item).contains(getString(R.string.clear_star))) {
                    return;
                }

                DbHelper.clearHis(getContext());

                loadRows();
            }
        }
    }

    // private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
    //     @Override
    //     public void onItemSelected(
    //             Presenter.ViewHolder itemViewHolder,
    //             Object item,
    //             RowPresenter.ViewHolder rowViewHolder,
    //             Row row) {
    //         if (item instanceof VodInfo) {
    //             // mBackgroundUri = ((VodInfo) item).getVod_pic();
    //             // startBackgroundTimer();
    //         }
    //     }
    // }

    // private class UpdateBackgroundTask extends TimerTask {
    //     @Override
    //     public void run() {
    //         mHandler.post(() -> updateBackground(mBackgroundUri));
    //     }
    // }


    @Override
    public void onResume() {
        super.onResume();
        loadRows();
    }

    private class GridItemPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(
                    ContextCompat.getColor(getContext(), R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }

}
