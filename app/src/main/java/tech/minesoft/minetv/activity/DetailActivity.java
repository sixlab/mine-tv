package tech.minesoft.minetv.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.FocusHighlightHelper;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.ListRow;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.base.BaseActivity;
import tech.minesoft.minetv.bean.MineMovieInfo;
import tech.minesoft.minetv.greendao.DaoHelper;
import tech.minesoft.minetv.presenter.EpisodeGroupPresenter;
import tech.minesoft.minetv.presenter.EpisodeItemPresenter;
import tech.minesoft.minetv.presenter.MinePresenterSelector;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.utils.ListUtils;
import tech.minesoft.minetv.utils.MineCallback;
import tech.minesoft.minetv.utils.RetrofitHelper;
import tech.minesoft.minetv.utils.RetrofitService;
import tech.minesoft.minetv.utils.SizeUtils;
import tech.minesoft.minetv.vo.MovieListVo;
import tech.minesoft.minetv.vo.UrlInfo;
import tech.minesoft.minetv.widget.TabVerticalGridView;
import tech.minesoft.minetv.widget.focus.MyItemBridgeAdapter;

public class DetailActivity extends BaseActivity implements View.OnClickListener,View.OnKeyListener {
    private static final String TAG = "VideoDetailActivity";

    private TabVerticalGridView mVerticalGridView;

    private ArrayObjectAdapter mEpisodesAdapter;
    private ArrayObjectAdapter mEpisodeGroupAdapter;

    private int mCurrentGroupPosition = 0;
    private String currentGroup = "";

    private MineMovieInfo currentInfo;
    private Map<String, List<UrlInfo>> vodMap = new HashMap<>();
    
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

        long id = (long) getIntent().getSerializableExtra(Const.SELECT_MOVIE_ID);

        currentInfo = DaoHelper.getInfo(id);

        initView();
        loadData();
        initListener();

        updateInfo();
    }

    private void reload() {
        loadData();
    }

    private void initView() {
        ImageView mPic = findViewById(R.id.iv_video_pic);
        
        Glide.with(this)
                .load(currentInfo.getVod_pic())
                .apply(new RequestOptions()
                        .override(SizeUtils.dp2px(this, 150),
                                SizeUtils.dp2px(this, 240))
                        .placeholder(R.drawable.load))
                .into(mPic);

//        mPic = findViewById(R.id.iv_logo_pic);
//        Glide.with(this)
//                .load(getDrawable(R.drawable.icon_logo))
//                .apply(new RequestOptions()
//                        .override(SizeUtils.dp2px(this, 150),
//                                SizeUtils.dp2px(this, 240))
//                        .placeholder(R.drawable.load))
//                .into(mPic);


        TextView mIntro = findViewById(R.id.tv_video_name);
        mIntro.setText(currentInfo.getVod_name());

        mIntro = findViewById(R.id.tv_video_info);
        mIntro.setText(String.format("%s\n%s：%s\n导演：%s\n主演：%s", currentInfo.getVod_remarks(),
                currentInfo.getVod_year(), currentInfo.getType_name(), currentInfo.getVod_director(),
                currentInfo.getVod_actor()));

        mIntro = findViewById(R.id.tv_video_intro);
        mIntro.setText(Html.fromHtml(currentInfo.getVod_content()));

        mTvUpdate = findViewById(R.id.tv_update);
        mTvStar = findViewById(R.id.tv_star);
        mTvClean = findViewById(R.id.tv_clean);
        mTvReverse = findViewById(R.id.tv_reverse);

        tvGroupName = findViewById(R.id.tv_episode_source);

        initEpisodes();
        initEpisodeGroup();
        
        initHidden();
    }
    
    private void loadData() {
        String playFrom = currentInfo.getVod_play_from();
        String playUrl = currentInfo.getVod_play_url();

        String[] groups = TextUtils.split(playFrom, "\\$\\$\\$");
        String[] groupsUrls = TextUtils.split(playUrl, "\\$\\$\\$");
        Map<String, String> links = ListUtils.split2Map(groups, groupsUrls);

        Map<String, Integer> map = DaoHelper.selectView(currentInfo.getId());

        List<String> validateGroup = new ArrayList<>();

        for (String group : RetrofitHelper.PLAY_FROM) {
            String groupUrls = links.get(group);
            if (!TextUtils.isEmpty(groupUrls)) {
                validateGroup.add(group);

                String[] urls = TextUtils.split(groupUrls, "#");

                if (currentInfo.getVod_reverse() == 1) {
                    List<String> list = Arrays.asList(urls);
                    Collections.reverse(list);
                    urls = list.toArray(new String[0]);
                }

                List<UrlInfo> infoList = new ArrayList<>();
                for (String url : urls) {
                    String[] urlInfos = TextUtils.split(url, "\\$");

                    UrlInfo info = new UrlInfo();
                    info.setVodId(currentInfo.getVod_id());
                    info.setInfoId(currentInfo.getId());
                    info.setUrl(url);
                    info.setUrls(urls);

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
        if (1 == currentInfo.getStar_flag()) {
            text = getString(R.string.action_unstar);
        } else {
            text = getString(R.string.action_star);
        }
        mTvStar.setText(text);
    }

    private void initListener() {
        mTvUpdate.setOnClickListener(this);
        mTvStar.setOnClickListener(this);
        mTvClean.setOnClickListener(this);
        mTvReverse.setOnClickListener(this);
    
        mTvClean.setOnKeyListener(this);
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

        // mVerticalGridView.addOnScrollListener(onScrollListener);
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
            ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(new EpisodeItemPresenter(this));

            arrayObjectAdapter.addAll(0, list);
            ListRow listRow = new ListRow(arrayObjectAdapter);
            addWithTryCatch(listRow);
        }
    }
    
    private void initHidden() {
        if(Integer.valueOf(1).equals(currentInfo.getVod_hide())){
            mTvClean.setTextColor(getColor(R.color.bl_purple));
        }else{
            mTvClean.setTextColor(getColor(R.color.colorWhite));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_update:
                updateInfo();
                reload();
                break;
            case R.id.tv_star:
                currentInfo = DaoHelper.changeStar(currentInfo.getId());
                showText("操作成功");
                reload();
                break;
            case R.id.tv_clean:
                DaoHelper.delViews(currentInfo.getId());
                showText("操作成功");
                reload();
                break;
            case R.id.tv_reverse:
                currentInfo = DaoHelper.changeReverse(currentInfo.getId());
                showText("操作成功");
                reload();
                break;
            default:
                break;
        }
    }
    
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_MENU && v.getId()==R.id.tv_clean) {
            currentInfo = DaoHelper.toggleVodHidden(currentInfo.getId());
            initHidden();
            showText("操作成功");
        }
        return false;
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
        RetrofitService service = RetrofitHelper.get(currentInfo.getApi_code());
        if (service != null) {
            service.detail(currentInfo.getVod_id()).enqueue(new MineCallback<MovieListVo>(this) {
                @Override
                public void finish(boolean success, MovieListVo body, String message) {
                    if (success) {
                        List<MineMovieInfo> list = body.getList();
                        if (null == list) {
                            showText("返回的list为null");
                        } else if (list.size() == 1) {
                            MineMovieInfo info = list.get(0);

                            currentInfo = DaoHelper.updateInfo(currentInfo.getId(), info);

                            showText("更新成功");

                            reload();
                        } else {
                            showText("返回的list长度为：" + list.size());
                        }
                    }
                }
            });
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

    public void clean(UrlInfo urlInfo) {
        DaoHelper.clearViews(urlInfo.getInfoId(), urlInfo.getGroupName(), urlInfo.getItemName());
        reload();
    }
}
