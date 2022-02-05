package tech.minesoft.minetv.utils;

import android.content.Context;
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
import tech.minesoft.minetv.widget.TextButton;

public class ScrollViewUtils {
    public static void addBlock(Context mContext, LinearLayout vodList, List<MineMovieInfo> list, BlockCallback<MineMovieInfo> callback) {
        LinearLayout line = null;
        for (int i = 0; i < list.size(); i++) {
            MineMovieInfo info = list.get(i);

            if (i % 5 == 0) {
                line = new LinearLayout(mContext);
                line.setOrientation(LinearLayout.HORIZONTAL);
                line.setLayoutParams(LayoutUtils.lineLayout);
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
        LinearLayout line = null;
        boolean first = true;
        for (int i = 0; i < list.size(); i++) {
            UrlInfo info = list.get(i);

            if (i % 10 == 0) {
                line = new LinearLayout(mContext);
                line.setOrientation(LinearLayout.HORIZONTAL);
                line.setLayoutParams(LayoutUtils.lineLayout);
                episodeList.addView(line);

                first = true;
            }

            TextButton btn = new TextButton(mContext);
            if (first) {
                first = false;
            } else {
                btn.setLayoutParams(LayoutUtils.btnLayout);
            }
            btn.setWidth(SizeUtils.dp2px(mContext, mContext.getResources().getDimension(R.dimen.widget_margin_5x)));
            btn.setText(info.getItemName());
            btn.setOnClickListener(callback.click(info));

            if (info.isViewed()) {
                btn.setNormalColor(R.color.mtv_viewed);
            }

            line.addView(btn);
        }
    }

}
