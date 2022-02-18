package tech.minesoft.minetv.v5app.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import tech.minesoft.minetv.v5app.R;
import tech.minesoft.minetv.v5app.bean.MineMovieInfo;
import tech.minesoft.minetv.v5app.bean.MineSiteInfo;
import tech.minesoft.minetv.v5app.databinding.ActivitySearchBinding;
import tech.minesoft.minetv.v5app.request.RequestService;
import tech.minesoft.minetv.v5app.utils.LayoutUtils;
import tech.minesoft.minetv.v5app.utils.MineCallback;
import tech.minesoft.minetv.v5app.utils.ScrollViewUtils;
import tech.minesoft.minetv.v5app.vo.MovieListVo;
import tech.minesoft.minetv.v5app.widget.TextButton;

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
        vodList.removeAllViews();

        if (null == list || list.size() == 0) {
            TextView textView = new TextView(this);
            textView.setText("搜索结果为空");
            textView.setLayoutParams(LayoutUtils.lineLayout);
            vodList.addView(textView);
            return;
        } else {
            TextView textView = new TextView(this);
            textView.setText(String.format("搜索结果:%d(%d/%d)", totalCount, page, totalPage));
            textView.setLayoutParams(LayoutUtils.lineLayout);
            vodList.addView(textView);
        }

        ScrollViewUtils.addBlock(this, vodList, list, null);

        if (totalPage > 1) {
            LinearLayout line = new LinearLayout(this);
            line.setOrientation(LinearLayout.HORIZONTAL);
            line.setLayoutParams(LayoutUtils.centerLayout);
            vodList.addView(line);

            if (page > 1) {
                TextButton prev = new TextButton(this);
                prev.setText("<");
                line.addView(prev);
                prev.setOnClickListener(view -> {
                    page--;
                    search();
                });
            }

            if (page < totalPage) {
                TextButton next = new TextButton(this);
                next.setLayoutParams(LayoutUtils.btnLayout);
                next.setText(">");
                line.addView(next);
                next.setOnClickListener(view -> {
                    page++;
                    search();
                });
            }
        }

        binding.content.getRoot().scrollTo(0, 0);
    }

}