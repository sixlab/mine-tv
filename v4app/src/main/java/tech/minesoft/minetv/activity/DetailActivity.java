package tech.minesoft.minetv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import tech.minesoft.minetv.bean.MineMovieInfo;
import tech.minesoft.minetv.bean.MineViewInfo;
import tech.minesoft.minetv.databinding.ActivityDetailBinding;
import tech.minesoft.minetv.greendao.DaoHelper;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.utils.LayoutUtils;
import tech.minesoft.minetv.utils.ListUtils;
import tech.minesoft.minetv.utils.MineCallback;
import tech.minesoft.minetv.utils.RetrofitHelper;
import tech.minesoft.minetv.utils.RetrofitService;
import tech.minesoft.minetv.utils.ScrollViewUtils;
import tech.minesoft.minetv.utils.SizeUtils;
import tech.minesoft.minetv.vo.MovieListVo;
import tech.minesoft.minetv.vo.UrlInfo;
import tech.minesoft.minetv.widget.TextButton;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private String currentGroup = "";
    private MineMovieInfo currentInfo;
    private List<String> validateGroup = new ArrayList<>();
    private Map<String, List<UrlInfo>> vodMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = this.getIntent().getExtras();

        long infoId = bundle.getLong(Const.SELECT_MOVIE_ID);
        String vodName = bundle.getString(Const.SELECT_MOVIE_NAME);

        binding.toolbar.setTitle(getString(R.string.title_detail, vodName));

        currentInfo = DaoHelper.getInfo(infoId);

        initInfoView();
        initListener();

        loadData();

        updateInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void initInfoView() {
        Glide.with(this)
                .load(currentInfo.getVod_pic())
                .apply(new RequestOptions()
                        .override(SizeUtils.dp2px(this, R.dimen.block_width),
                                SizeUtils.dp2px(this, R.dimen.block_img_height))
                        .placeholder(R.drawable.load))
                .into(binding.ivVideoPic);

        binding.tvVideoName.setText(
                String.format(
                        "%s [%s|%s|%s]",
                        currentInfo.getVod_name(),
                        currentInfo.getVod_year(),
                        currentInfo.getType_name(),
                        currentInfo.getVod_remarks()
                )
        );

        binding.tvVideoInfo.setText(
                String.format(
                        "导演：%s\n主演：%s",
                        currentInfo.getVod_director(),
                        currentInfo.getVod_actor()
                )
        );

        binding.tvVideoIntro.setText(Html.fromHtml(currentInfo.getVod_content()));
    }

    private void loadData() {
        String playFrom = currentInfo.getVod_play_from();
        String playUrl = currentInfo.getVod_play_url();

        String[] groups = TextUtils.split(playFrom, "\\$\\$\\$");
        String[] groupsUrls = TextUtils.split(playUrl, "\\$\\$\\$");
        Map<String, String> links = ListUtils.split2Map(groups, groupsUrls);

        Map<String, Integer> map = DaoHelper.selectView(currentInfo.getId());

        validateGroup.clear();

        MineViewInfo viewInfo = DaoHelper.selectLastView(currentInfo.getId());
        if (null != viewInfo) {
            currentGroup = viewInfo.getVod_from();
        }
        for (String group : groups) {
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

        if (validateGroup.size() > 0 && TextUtils.isEmpty(currentGroup)) {
            currentGroup = validateGroup.get(0);
        }
        renderEpisodes();

        String text;
        if (1 == currentInfo.getStar_flag()) {
            text = getString(R.string.action_unstar);
        } else {
            text = getString(R.string.action_star);
        }
        binding.tvStar.setText(text);
    }

    private void initListener() {
        binding.tvUpdate.setOnClickListener(view -> {
            updateInfo();
            loadData();
        });
        binding.tvStar.setOnClickListener(view -> {
            currentInfo = DaoHelper.changeStar(currentInfo.getId());
            showText("操作成功");
            loadData();
        });
        binding.tvClean.setOnClickListener(view -> {
            DaoHelper.delViews(currentInfo.getId());
            showText("操作成功");
            loadData();
        });
        binding.tvReverse.setOnClickListener(view -> {
            currentInfo = DaoHelper.changeReverse(currentInfo.getId());
            showText("操作成功");
            loadData();
        });
    }

    private void renderEpisodes() {
        // 分组
        binding.tvEpisodeSource.removeAllViews();
        boolean first = true;
        for (String group : validateGroup) {
            TextButton btn = new TextButton(this);
            if (first) {
                first = false;
            } else {
                btn.setLayoutParams(LayoutUtils.btnLayout);
            }
            btn.setText(group);
            btn.setOnClickListener(view -> {
                currentGroup = btn.getText().toString();
                renderEpisodes();
            });
            // btn.setLayoutParams(btnLayoutParams);
            if (group.equals(currentGroup)) {
                btn.setNormalColor(R.color.mtv_viewed);
            }
            binding.tvEpisodeSource.addView(btn);
        }

        // 剧集
        LinearLayout episodeList = binding.content.episodeList;
        episodeList.removeAllViews();

        List<UrlInfo> infoList = vodMap.get(currentGroup);
        if (null == infoList || infoList.size() == 0) {
            TextView textView = new TextView(this);
            textView.setText("结果为空");
            textView.setLayoutParams(LayoutUtils.lineLayout);
            episodeList.addView(textView);
            return;
        }

        ScrollViewUtils.addBtn(this, episodeList, infoList, info -> view -> {
            DaoHelper.addView(info);

            Intent intent = new Intent(DetailActivity.this, PlayerActivity.class);
            intent.putExtra(Const.SELECT_EPISODE, info);
            DetailActivity.this.startActivity(intent);
        });
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

                            loadData();
                        } else {
                            showText("返回的list长度为：" + list.size());
                        }
                    }
                }
            });
        }
    }

    private void showText(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}