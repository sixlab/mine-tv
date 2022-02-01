package tech.minesoft.minetv.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.bean.MineMovieInfo;
import tech.minesoft.minetv.databinding.ActivitySearchBinding;
import tech.minesoft.minetv.utils.SizeUtils;
import tech.minesoft.minetv.widget.ImageBlock;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;

    private String keyword;
    private Integer page = 0;
    private Integer totalPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setTitle(R.string.title_search);

        binding.searchBtn.setOnClickListener(view -> search());
        binding.searchInput.setOnKeyListener((view, keyCode, keyEvent) -> {
            if(keyCode==KeyEvent.KEYCODE_ENTER){
                return search();
            }
            return false;
        });

        binding.searchInput.requestFocus();
    }

    public boolean search(){
        List<MineMovieInfo> list = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            MineMovieInfo info = new MineMovieInfo();
            info.setId(123L);
            info.setVod_name("zhang" + i);
            info.setVod_pic("https://img.52swat.cn/upload/vod/20220201-1/8577bc3610c8c1d732efeacad3a4282d.jpg");
            info.setVod_director("dir ++" + i);
            list.add(info);
        }
        addResult(10, list);
//        keyword = binding.searchInput.getText().toString().trim();
//        if(TextUtils.isEmpty(keyword)){
//            return false;
//        }
//
//        RequestService.request(keyword,1, new MineCallback<MovieListVo>(SearchActivity.this) {
//            @Override
//            public void finish(boolean success, MovieListVo body, String message) {
//                if (success) {
//                    List<MineMovieInfo> list = body.getList();
//                    if (null != list && list.size() > 0) {
//                        totalPage = body.getPagecount();
//                        page = Integer.valueOf(body.getPage());
//
//                        addResult(body.getTotal(), list);
//                    } else {
//                        // TODO null
//                    }
//                }
//            }
//        });

        return true;
    }

    private void addResult(Integer total, List<MineMovieInfo> list) {
        LinearLayout vodList = binding.content.vodList;
        SearchActivity mContext = SearchActivity.this;

        vodList.removeAllViews();

        LinearLayout line = null;
        for (int i = 0; i < list.size(); i++) {
            MineMovieInfo info = list.get(i);

            if(i%5==0){
                line = new LinearLayout(mContext);
                line.setOrientation(LinearLayout.HORIZONTAL);
                vodList.addView(line);
            }

            ImageBlock block = new ImageBlock(mContext);

            ImageView iv = block.findViewById(R.id.vod_pic);
            Glide.with(mContext)
                    .load(info.getVod_pic())
                    .apply(new RequestOptions()
                            .override(SizeUtils.dp2px(mContext, R.dimen.block_img_width),
                                    SizeUtils.dp2px(mContext, R.dimen.block_img_height))
                            .placeholder(R.drawable.load))
                    .into(iv);

            ((TextView)block.findViewById(R.id.vod_title)).setText(info.getVod_name());
            ((TextView)block.findViewById(R.id.vod_desc)).setText(info.getVod_director());

            line.addView(block);
        }
    }
}