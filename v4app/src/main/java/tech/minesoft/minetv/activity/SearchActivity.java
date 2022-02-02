package tech.minesoft.minetv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.bean.MineMovieInfo;
import tech.minesoft.minetv.bean.MineSiteInfo;
import tech.minesoft.minetv.databinding.ActivitySearchBinding;
import tech.minesoft.minetv.databinding.SearchScrollingBinding;
import tech.minesoft.minetv.greendao.DaoHelper;
import tech.minesoft.minetv.request.RequestService;
import tech.minesoft.minetv.utils.ScrollViewUtils;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.utils.MineCallback;
import tech.minesoft.minetv.vo.MovieListVo;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;

    private Integer page = 1;
    private Integer totalPage = 1;
    private Integer totalCount = 0;

    private String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setTitle(R.string.title_search);

        binding.searchInput.setOnKeyListener((view, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                keyword = binding.searchInput.getText().toString().trim();
                if (TextUtils.isEmpty(keyword)) {
                    return false;
                }

                InputMethodManager imm = SearchActivity.this.getSystemService(InputMethodManager.class);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), 0);
                }

                page = 1;
                totalPage = 1;
                totalCount = 0;

                return search();
            }
            return false;
        });

        binding.searchInput.requestFocus();
    }

    public boolean search() {
        RequestService.request(keyword, page, new MineCallback<MovieListVo>(SearchActivity.this) {
            @Override
            public void finish(boolean success, MovieListVo body, String message) {
                if (success) {
                    List<MineMovieInfo> list = body.getList();
                    MineSiteInfo activeSite = RequestService.activeSite();

                    for (MineMovieInfo item : list) {
                        item.setApi_code(activeSite.getCode());
                        item.setApi_name(activeSite.getName());
                        item.setApi_url(activeSite.getUrl());
                    }

                    page = Integer.valueOf(body.getPage());
                    totalPage = body.getPagecount();
                    totalCount = body.getTotal();

                    addResult(list);
                }
            }
        });

        return true;
    }

    private void addResult(List<MineMovieInfo> list) {
        LinearLayout vodList = binding.content.vodList;
        SearchActivity mContext = SearchActivity.this;
        LinearLayout.LayoutParams params = ScrollViewUtils.layoutParams(mContext);

        vodList.removeAllViews();

        if (null == list || list.size() == 0) {
            TextView textView = new TextView(mContext);
            textView.setText("搜索结果为空");
            textView.setLayoutParams(params);
            vodList.addView(textView);
            return;
        } else {
            TextView textView = new TextView(mContext);
            textView.setText(String.format("搜索结果:%d(%d/%d)", totalCount, page, totalPage));
            textView.setLayoutParams(params);
            vodList.addView(textView);
        }

        ScrollViewUtils.addBlock(mContext, vodList, list, info -> view -> {
            long infoId = DaoHelper.saveInfo(info);

            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(Const.SELECT_MOVIE_ID, infoId);
            intent.putExtra(Const.SELECT_MOVIE_NAME, info.getVod_name());
            mContext.startActivity(intent);
        });

        if (totalPage > 1) {
            LinearLayout line = new LinearLayout(mContext);
            line.setOrientation(LinearLayout.HORIZONTAL);
            line.setLayoutParams(params);
            vodList.addView(line);

            if (page > 1) {
                Button prev = new Button(mContext);
                prev.setText("<");
                line.addView(prev);
                prev.setOnClickListener(view -> {
                    page--;
                    search();
                });
            }

            if (page < totalPage) {
                Button next = new Button(mContext);
                next.setText(">");
                line.addView(next);
                next.setOnClickListener(view -> {
                    page++;
                    search();
                });
            }
        }

        SearchScrollingBinding content = binding.content;
        content.getRoot().scrollTo(0, 0);
    }
}