package tech.minesoft.minetv.utils;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.bean.MineMovieInfo;
import tech.minesoft.minetv.vo.UrlInfo;
import tech.minesoft.minetv.widget.ImageBlock;

public class ScrollViewUtils {
    private static LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private static boolean init = false;

    public static LinearLayout.LayoutParams layoutParams(Context mContext) {
        init(mContext);
        return layoutParams;
    }

    public static void addBlock(Context mContext, LinearLayout vodList, List<MineMovieInfo> list, BlockCallback<MineMovieInfo> callback) {
        init(mContext);

        LinearLayout line = null;
        for (int i = 0; i < list.size(); i++) {
            MineMovieInfo info = list.get(i);

            if (i % 5 == 0) {
                line = new LinearLayout(mContext);
                line.setOrientation(LinearLayout.HORIZONTAL);
                line.setLayoutParams(layoutParams);
                vodList.addView(line);
            }

            ImageBlock block = new ImageBlock(mContext);

            ImageView iv = block.findViewById(R.id.vod_pic);
            Glide.with(mContext)
                    .load(info.getVod_pic())
                    .apply(new RequestOptions()
                            .override(SizeUtils.dp2px(mContext, R.dimen.block_width),
                                    SizeUtils.dp2px(mContext, R.dimen.block_img_height))
                            .placeholder(R.drawable.load))
                    .into(iv);

            ((TextView) block.findViewById(R.id.vod_title)).setText(info.getVod_name());
            ((TextView) block.findViewById(R.id.vod_desc)).setText(String.format("[%s] %s", info.getVod_year(), info.getVod_director()));


            block.setOnClickListener(callback.click(info));

            line.addView(block);
        }
    }

    public static void addBtn(Context mContext, LinearLayout episodeList, List<UrlInfo> list, BlockCallback<UrlInfo> callback) {
        init(mContext);

        LinearLayout.LayoutParams btnLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int dimension = (int) mContext.getResources().getDimension(R.dimen.widget_margin_1x);
        btnLayoutParams.setMargins(0, 0, dimension, dimension);


        LinearLayout line = null;
        for (int i = 0; i < list.size(); i++) {
            UrlInfo info = list.get(i);

            if (i % 10 == 0) {
                line = new LinearLayout(mContext);
                line.setOrientation(LinearLayout.HORIZONTAL);
                line.setLayoutParams(layoutParams);
                episodeList.addView(line);
            }

            Button btn = new Button(mContext);
            btn.setText(info.getItemName());
            btn.setOnClickListener(callback.click(info));
            btn.setLayoutParams(btnLayoutParams);

            btn.setTextColor(mContext.getColor(R.color.white));
            if(info.isViewed()){
                btn.setBackgroundColor(mContext.getColor(R.color.mtv_viewed));
            }else{
                btn.setBackgroundColor(mContext.getColor(R.color.mtv_btn_normal));
            }

            line.addView(btn);
        }
    }

    private static void init(Context mContext) {
        if (!init) {
            init = true;

            int dimension = (int) mContext.getResources().getDimension(R.dimen.widget_margin_1x);
            layoutParams.setMargins(dimension, 0, dimension, 0);
        }
    }
}
