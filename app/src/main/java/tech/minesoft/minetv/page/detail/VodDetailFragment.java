package tech.minesoft.minetv.page.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.leanback.app.DetailsFragment;
import androidx.leanback.app.DetailsFragmentBackgroundController;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import androidx.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnActionClickedListener;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import tech.minesoft.minetv.MainActivity;
import tech.minesoft.minetv.R;
import tech.minesoft.minetv.data.DbHelper;
import tech.minesoft.minetv.data.RequestHelper;
import tech.minesoft.minetv.page.BlockPresenter;
import tech.minesoft.minetv.page.play.PlaybackActivity;
import tech.minesoft.minetv.utils.MineCallback;
import tech.minesoft.minetv.vo.UrlInfo;
import tech.minesoft.minetv.vo.VodInfo;
import tech.minesoft.minetv.vo.VodListVo;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/*
 * LeanbackDetailsFragment extends DetailsFragment, a Wrapper fragment for leanback details screens.
 * It shows a detailed view of video and its meta plus related videos.
 */
public class VodDetailFragment extends DetailsFragment {
    private static final String TAG = "VideoDetailsFragment";

    private static final int ACTION_UPDATE = 1;
    private static final int ACTION_STAR = 2;
    private static final int ACTION_HIS = 3;
    private static final int ACTION_VIEWED = 4;
    private static final int ACTION_REVERSE = 5;

    private static final int DETAIL_THUMB_WIDTH = 274;
    private static final int DETAIL_THUMB_HEIGHT = 274;

    private VodInfo mSelectedMovie;

    private ArrayObjectAdapter mAdapter;
    private ClassPresenterSelector mPresenterSelector;

    private View clickedView;
    private boolean reverse = false;

    private DetailsFragmentBackgroundController mDetailsBackground;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate DetailsFragment");
        super.onCreate(savedInstanceState);

        mDetailsBackground = new DetailsFragmentBackgroundController(this);

        mSelectedMovie = (VodInfo) getActivity().getIntent().getSerializableExtra(VodDetailActivity.MOVIE);
        if (mSelectedMovie != null) {
            reverse = DbHelper.needReverse(getContext(), mSelectedMovie.getVod_id());

            mPresenterSelector = new ClassPresenterSelector();
            mAdapter = new ArrayObjectAdapter(mPresenterSelector);
            setupDetailsOverviewRow();
            setupDetailsOverviewRowPresenter();
            setupRelatedMovieListRow();
            setAdapter(mAdapter);
            initializeBackground(mSelectedMovie);
            setOnItemViewClickedListener(new ItemViewClickedListener());
        } else {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }

    public void reloadUI() {
        try{
            ViewGroup.LayoutParams layoutParams = getActivity().findViewById(R.id.details_root).getLayoutParams();
            Log.d(TAG, "onCreate DetailsFragment:" + layoutParams.height);
            Log.d(TAG, "onCreate DetailsFragment:" + layoutParams.height);

            mAdapter.clear();

            setupDetailsOverviewRow();
            // setupDetailsOverviewRowPresenter();
            setupRelatedMovieListRow();
            // setAdapter(mAdapter);
            // initializeBackground(mSelectedMovie);
            // setOnItemViewClickedListener(new ItemViewClickedListener());

            if (clickedView != null) {
                clickedView.requestFocus();
            }
        }catch (Exception e){
            e.printStackTrace();
            // Toast.makeText(getContext(), "更新界面异常", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        reloadUI();
    }

    private void initializeBackground(VodInfo data) {
        mDetailsBackground.enableParallax();
        Glide.with(getActivity()).load(data.getVod_pic()).asBitmap().centerCrop().error(R.drawable.default_background).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                mDetailsBackground.setCoverBitmap(bitmap);
                mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
            }
        });
    }

    private void setupDetailsOverviewRow() {
        Log.d(TAG, "doInBackground: " + mSelectedMovie.toString());
        final DetailsOverviewRow row = new DetailsOverviewRow(mSelectedMovie);
        row.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.default_background));
        int width = convertDpToPixel(getActivity().getApplicationContext(), DETAIL_THUMB_WIDTH);
        int height = convertDpToPixel(getActivity().getApplicationContext(), DETAIL_THUMB_HEIGHT);

        Glide.with(getActivity()).load(mSelectedMovie.getVod_pic()).centerCrop().error(R.drawable.default_background).into(new SimpleTarget<GlideDrawable>(width, height) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                Log.d(TAG, "details overview card image url ready: " + resource);
                row.setImageDrawable(resource);
                mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
            }
        });

        ArrayObjectAdapter actionAdapter = new ArrayObjectAdapter();

        if(DbHelper.isStar(getContext(), mSelectedMovie.getVod_id())){
            actionAdapter.add(new Action(ACTION_STAR, getResources().getString(R.string.action_star_not)));
        }else{
            actionAdapter.add(new Action(ACTION_STAR, getResources().getString(R.string.type_star)));
        }

        actionAdapter.add(new Action(ACTION_UPDATE, getResources().getString(R.string.action_update)));

        actionAdapter.add(new Action(ACTION_HIS, getResources().getString(R.string.clear_his)));

        actionAdapter.add(new Action(ACTION_VIEWED, getResources().getString(R.string.clear_view)));

        actionAdapter.add(new Action(ACTION_REVERSE, getResources().getString(R.string.action_reverse)));

        row.setActionsAdapter(actionAdapter);

        mAdapter.add(row);
    }

    private void setupDetailsOverviewRowPresenter() {
        final Context context = getContext();

        // Set detail background.
        FullWidthDetailsOverviewRowPresenter detailsPresenter = new FullWidthDetailsOverviewRowPresenter(new DetailsDescriptionPresenter());
        detailsPresenter.setBackgroundColor(ContextCompat.getColor(context, R.color.bl_blue));

        // Hook up transition element.
        FullWidthDetailsOverviewSharedElementHelper sharedElementHelper = new FullWidthDetailsOverviewSharedElementHelper();
        sharedElementHelper.setSharedElementEnterTransition(getActivity(), VodDetailActivity.SHARED_ELEMENT_NAME);
        detailsPresenter.setListener(sharedElementHelper);
        detailsPresenter.setParticipatingEntranceTransition(true);

        detailsPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                if (action.getId() == ACTION_UPDATE) {
                    updateUrl();
                    // } else if (action.getId() == ACTION_VIEWED) {
                    //
                } else {
                    String msg = "";
                    Integer vodId = mSelectedMovie.getVod_id();

                    if (action.getId() == ACTION_STAR) {
                        if (DbHelper.isStar(context, vodId)) {
                            msg = getString(R.string.action_star_not);
                            DbHelper.delStar(getContext(), vodId);
                        }else{
                            msg = getString(R.string.type_star);
                            DbHelper.insertStar(context, mSelectedMovie);
                        }

                        reloadUI();
                    } else if (action.getId() == ACTION_HIS) {
                        msg = getString(R.string.clear_his);
                        DbHelper.delHis(context, vodId);
                    } else if (action.getId() == ACTION_VIEWED) {
                        msg = getString(R.string.clear_view);
                        DbHelper.delViews(context, vodId);

                        reloadUI();
                    } else if (action.getId() == ACTION_REVERSE) {
                        msg = getString(R.string.action_reverse);

                        reverse = !reverse;
                        DbHelper.updateReverse(context, vodId, reverse);

                        reloadUI();
                    }

                    Toast.makeText(context, msg + " done", Toast.LENGTH_LONG).show();
                }
            }
        });
        mPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsPresenter);
    }

    private void updateUrl() {
        if (mSelectedMovie.getVod_id() < 0) {
            return;
        }
        RequestHelper.service.detail(mSelectedMovie.getVod_id()).enqueue(new MineCallback<VodListVo>() {
            @Override
            public void success(VodListVo body) {
                List<VodInfo> list = body.getList();
                if (null == list) {
                    Toast.makeText(getContext(), "返回的list为null", Toast.LENGTH_LONG).show();
                } else if (list.size() == 1) {
                    VodInfo info = list.get(0);
                    mSelectedMovie = info;

                    DbHelper.updateInfo(getContext(), info);

                    Toast.makeText(getContext(), "更新成功", Toast.LENGTH_LONG).show();

                    reloadUI();
                } else {
                    Toast.makeText(getContext(), "返回的list长度为：" + list.size(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setupRelatedMovieListRow() {
        /*
"vod_play_from": "zkyun$$$zkm3u8$$$33uu$$$33uuck$$$kuyun$$$ckm3u8",
"vod_play_server": "no$$$no$$$no$$$no$$$no$$$no",
"vod_play_note": "$$$$$$$$$$$$$$$",
"vod_play_url": "蓝光1080P$https://zk.sd-dykj.com/share/BGLpBLhHvAeK8wYS$$$蓝光1080P$https://zk.sd-dykj.com/2020/07/02/BGLpBLhHvAeK8wYS/playlist.m3u8$$$蓝光1080P$https://zk.sd-dykj.com/share/BGLpBLhHvAeK8wYS$$$蓝光1080P$https://zk.sd-dykj.com/2020/07/02/BGLpBLhHvAeK8wYS/playlist.m3u8$$$HD中字$https://iqiyi.cdn27-okzy.com/share/8153349b1eb6f6c01622d4f395993bfe$$$HD中字$https://iqiyi.cdn27-okzy.com/20200702/5586_debea6cd/index.m3u8",
         */

        String playFrom = mSelectedMovie.getVod_play_from();
        String playUrl = mSelectedMovie.getVod_play_url();

        String[] groups = TextUtils.split(playFrom, "\\$\\$\\$");
        String[] groupsUrls = TextUtils.split(playUrl, "\\$\\$\\$");

        Map<String, Integer> map = DbHelper.queryView(getContext(), mSelectedMovie.getVod_id());

        for (int i = 0; i < groups.length; i++) {
            String group = groups[i];
            String groupUrls = groupsUrls[i];   //   蓝光1080P$https://zk.sd-dykj.com/share/BGLpBLhHvAeK8wYS#蓝光1080P$https://zk.sd-dykj.com/share/BGLpBLhHvAeK8wYS

            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new BlockPresenter());
            String[] urls = TextUtils.split(groupUrls, "#");
            if(reverse){
                List<String> urlsList = Arrays.asList(urls);
                Collections.reverse(urlsList);
                urls = urlsList.toArray(urls);
            }

            for (String url : urls) {
                //   蓝光1080P$https://zk.sd-dykj.com/share/BGLpBLhHvAeK8wYS
                String[] urlInfos = TextUtils.split(url, "\\$");

                UrlInfo info = new UrlInfo();
                info.setVodId(mSelectedMovie.getVod_id());

                if (urlInfos.length == 2) {
                    info.setVodName(mSelectedMovie.getVod_name());
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

                listRowAdapter.add(info);

            }

            HeaderItem header = new HeaderItem(0, group);
            mAdapter.add(new ListRow(header, listRowAdapter));
            mPresenterSelector.addClassPresenter(ListRow.class, new ListRowPresenter());
        }
    }

    private int convertDpToPixel(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            clickedView = rowViewHolder.view;

            if (item instanceof UrlInfo) {
                UrlInfo info = (UrlInfo) item;
                if ("Error".equals(info.getPlayUrl())) {
                    Toast.makeText(getContext(), info.getGroupName() + ":" + info.getVodName(), Toast.LENGTH_LONG).show();
                } else {
                    DbHelper.insertView(getContext(), info.getVodId(), info.getGroupName(), info.getItemName());

                    Intent intent = new Intent(getActivity(), PlaybackActivity.class);
                    intent.putExtra(VodDetailActivity.MOVIE, info);
                    startActivity(intent);
                }
            }
        }
    }
}
