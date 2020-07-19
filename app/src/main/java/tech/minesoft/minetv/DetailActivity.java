package tech.minesoft.minetv;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.FocusHighlightHelper;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.ListRow;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.minesoft.minetv.data.DbHelper;
import tech.minesoft.minetv.data.RequestHelper;
import tech.minesoft.minetv.presenter.MinePresenterSelector;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.utils.ListUtils;
import tech.minesoft.minetv.utils.MineCallback;
import tech.minesoft.minetv.utils.SizeUtils;
import tech.minesoft.minetv.v2.base.BaseActivity;
import tech.minesoft.minetv.v2.widgets.TabVerticalGridView;
import tech.minesoft.minetv.v2.widgets.focus.MyItemBridgeAdapter;
import tech.minesoft.minetv.vo.UrlInfo;
import tech.minesoft.minetv.vo.VodInfo;
import tech.minesoft.minetv.vo.VodListVo;
import tech.minesoft.minetv.widget.EpisodeGroupPresenter;
import tech.minesoft.minetv.widget.EpisodeItemPresenter;

public class DetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "VideoDetailActivity";

    private TabVerticalGridView mVerticalGridView;
    private ArrayObjectAdapter mEpisodesAdapter;
    private ArrayObjectAdapter mEpisodeGroupAdapter;

    private int mCurrentGroupPosition = 0;
    private String currentGroup = "";

    private VodInfo currentInfo;
    private Map<String, List<UrlInfo>> vodMap = new HashMap<>();
    private boolean needReverse = false;

    private TextView mTvUpdate;
    private TextView mTvStar;
    private TextView mTvClean;
    private TextView mTvReverse;

    private TextView tvGroupName;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        currentInfo = (VodInfo) getIntent().getSerializableExtra(Const.SELECT_MOVIE);

        initView();
        loadData();
        initListener();
    }

    private void reload() {
        loadData();
    }

    private void initView() {
        Glide.with(this)
                .load(currentInfo.getVod_pic())
                .apply(new RequestOptions()
                        // .override(SizeUtils.dp2px(this, 125),
                        //         SizeUtils.dp2px(this, 185))
                        .placeholder(R.drawable.load))
                .into((ImageView) findViewById(R.id.iv_video_pic));

        TextView textView;

        textView = findViewById(R.id.tv_video_name);
        textView.setText(currentInfo.getVod_name());

        textView = findViewById(R.id.tv_year);
        textView.setText(String.format("%s：%s", currentInfo.getVod_year(), currentInfo.getType_name()));

        textView = findViewById(R.id.tv_director);
        textView.setText(String.format("导演：%s", currentInfo.getVod_director()));

        textView = findViewById(R.id.tv_video_actors);
        textView.setText(String.format("主演：%s", currentInfo.getVod_actor()));

        textView = findViewById(R.id.tv_video_introduction);
        textView.setText(String.format("简介：%s", currentInfo.getVod_content()));


        mTvUpdate = findViewById(R.id.tv_update);
        mTvStar = findViewById(R.id.tv_star);
        mTvClean = findViewById(R.id.tv_clean);
        mTvReverse = findViewById(R.id.tv_reverse);

        tvGroupName = findViewById(R.id.tv_episode);

        initEpisodes();
        initEpisodeGroup();
    }

    private void loadData() {

        String playFrom = currentInfo.getVod_play_from();
        String playUrl = currentInfo.getVod_play_url();

        String[] groups = TextUtils.split(playFrom, "\\$\\$\\$");
        String[] groupsUrls = TextUtils.split(playUrl, "\\$\\$\\$");

        Map<String, Integer> map = DbHelper.selectView(this, currentInfo.getVod_id());
        needReverse = DbHelper.needReverse(this, currentInfo.getVod_id());

        List<String> validateGroup = new ArrayList<>();
        for (int i = 0; i < groups.length; i++) {
            String group = groups[i];
            String groupUrls = groupsUrls[i];
            if (RequestHelper.PLAY_FROM.contains(group)) {
                validateGroup.add(group);

                String[] urls = TextUtils.split(groupUrls, "#");

                if (needReverse) {
                    List<String> list = Arrays.asList(urls);
                    Collections.reverse(list);
                    urls = list.toArray(new String[0]);
                }

                List<UrlInfo> infoList = new ArrayList<>();
                for (String url : urls) {
                    String[] urlInfos = TextUtils.split(url, "\\$");

                    UrlInfo info = new UrlInfo();
                    info.setVodId(currentInfo.getVod_id());

                    if (urlInfos.length == 2) {
                        info.setVodName(currentInfo.getVod_name());
                        info.setGroupName(group);
                        info.setItemName(urlInfos[0]);
                        info.setPlayUrl(urlInfos[1]);

                        String key = MessageFormat.format("{0}${1}", group, info.getItemName());
                        info.setViewed(map.containsKey(key));
                    } else {
                        info.setVodName(url);
                        info.setGroupName(String.valueOf(urlInfos.length));
                        info.setItemName("Error");
                        info.setPlayUrl("Error");
                        info.setViewed(false);
                    }
                    infoList.add(info);
                }

                vodMap.put(group, infoList);
            }
        }
        mEpisodeGroupAdapter.clear();
        mEpisodeGroupAdapter.addAll(0, validateGroup);

        if (validateGroup.size() > 0 && TextUtils.isEmpty(currentGroup)) {
            currentGroup = validateGroup.get(0);
        }
        addEpisodes();

        String text;
        if (DbHelper.isStar(this, currentInfo.getVod_id())) {
            text = getString(R.string.action_star);
        } else {
            text = getString(R.string.action_unstar);
        }
        mTvStar.setText(text);
    }

    private void initListener() {
        mTvUpdate.setOnClickListener(this);
        mTvStar.setOnClickListener(this);
        mTvClean.setOnClickListener(this);
        mTvReverse.setOnClickListener(this);
    }

    private void initEpisodes() {
        mVerticalGridView = findViewById(R.id.hg_episode_items);
        mVerticalGridView.setBackReturn(false);
        // mVerticalGridView.setTabView(mActivity.getHorizontalGridView());
        // mVerticalGridView.setGroup(mActivity.getGroup());
        mVerticalGridView.setVerticalSpacing(SizeUtils.dp2px(this, 6));
        MinePresenterSelector presenterSelector = new MinePresenterSelector();
        mEpisodesAdapter = new ArrayObjectAdapter(presenterSelector);
        ItemBridgeAdapter itemBridgeAdapter = new ItemBridgeAdapter(mEpisodesAdapter);
        mVerticalGridView.setAdapter(itemBridgeAdapter);

        mVerticalGridView.addOnScrollListener(onScrollListener);
        // mVerticalGridView.addOnChildViewHolderSelectedListener(onSelectedListener);

        FocusHighlightHelper.setupBrowseItemFocusHighlight(itemBridgeAdapter, FocusHighlight.ZOOM_FACTOR_MEDIUM, false);
    }

    private void initEpisodeGroup() {
        HorizontalGridView mHgEpisodeGroup = findViewById(R.id.hg_play_from);
        mHgEpisodeGroup.setItemAnimator(null);
        // mHgEpisodeGroup.setFocusScrollStrategy(HorizontalGridView.FOCUS_SCROLL_ITEM);
        mHgEpisodeGroup.setHorizontalSpacing(SizeUtils.dp2px(this, 6));
        mEpisodeGroupAdapter = new ArrayObjectAdapter(new EpisodeGroupPresenter());
        ItemBridgeAdapter itemBridgeAdapter = new MyItemBridgeAdapter(mEpisodeGroupAdapter) {
            @Override
            public OnItemFocusChangedListener getOnItemFocusChangedListener() {
                return (focusView, itemViewHolder, item, hasFocus, pos) -> {
                    if (mCurrentGroupPosition != pos) {
                        mCurrentGroupPosition = pos;
                        if (item instanceof String) {
                            currentGroup = item.toString();
                            addEpisodes();
                        }
                    }
                };
            }
        };

        mHgEpisodeGroup.setAdapter(itemBridgeAdapter);
        FocusHighlightHelper.setupBrowseItemFocusHighlight(itemBridgeAdapter, FocusHighlight.ZOOM_FACTOR_LARGE, false);
    }

    private void addEpisodes() {
        mEpisodesAdapter.clear();

        tvGroupName.setText(getString(R.string.select_group, currentGroup));

        List<UrlInfo> infoList = vodMap.get(currentGroup);

        List<List> listList = ListUtils.splitList(infoList, 8);

        for (List<UrlInfo> list : listList) {
            ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(new EpisodeItemPresenter());

            arrayObjectAdapter.addAll(0, list);
            ListRow listRow = new ListRow(arrayObjectAdapter);
            addWithTryCatch(listRow);
        }
    }

    private final RecyclerView.OnScrollListener onScrollListener
            = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
                case RecyclerView.SCROLL_STATE_SETTLING:
                    //由于用户的操作，屏幕产生惯性滑动，停止加载图片
                    Glide.with(DetailActivity.this).pauseRequests();
                    break;
                case RecyclerView.SCROLL_STATE_IDLE:
                    Glide.with(DetailActivity.this).resumeRequests();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_update:
                updateInfo();
                reload();
                break;
            case R.id.tv_star:
                DbHelper.changeStar(this, currentInfo.getVod_id());
                showText("操作成功");
                reload();
                break;
            case R.id.tv_clean:
                DbHelper.delViews(this, currentInfo.getVod_id());
                showText("操作成功");
                reload();
                break;
            case R.id.tv_reverse:
                DbHelper.updateReverse(this, currentInfo.getVod_id(), !needReverse);
                showText("操作成功");
                reload();
                break;
            default:
                break;
        }
    }

    private void addWithTryCatch(Object item) {
        try {
            if (!mVerticalGridView.isComputingLayout()) {
                mEpisodesAdapter.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateInfo() {
        RequestHelper.service.detail(currentInfo.getVod_id()).enqueue(new MineCallback<VodListVo>(this) {
            @Override
            public void finish(boolean success, VodListVo body, String message) {
                if (success) {
                    List<VodInfo> list = body.getList();
                    if (null == list) {
                        showText("返回的list为null");
                    } else if (list.size() == 1) {
                        VodInfo info = list.get(0);
                        currentInfo = info;

                        DbHelper.updateInfo(DetailActivity.this, info);

                        showText("更新成功");

                        reload();
                    } else {
                        showText("返回的list长度为：" + list.size());
                    }
                }
            }
        });
    }

    private Toast toast = null;

    @SuppressLint("ShowToast")
    public void showText(CharSequence text) {
        try {
            toast.getView().isShown();
            toast.setText(text);
        } catch (Exception e) {
            toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        }
        toast.show();
    }
}
