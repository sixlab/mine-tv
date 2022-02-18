package tech.minesoft.minetv.v4app.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import tech.minesoft.minetv.v4app.R;
import tech.minesoft.minetv.v4app.activity.DetailActivity;
import tech.minesoft.minetv.v4app.bean.MineMovieInfo;
import tech.minesoft.minetv.v4app.greendao.DaoHelper;
import tech.minesoft.minetv.v4app.widget.ImageBlock;

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

            block.setOnClickListener(view -> {
                long infoId = DaoHelper.saveInfo(info);

                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(Const.SELECT_MOVIE_ID, infoId);
                intent.putExtra(Const.SELECT_MOVIE_NAME, info.getVod_name());
                mContext.startActivity(intent);
            });
            if (null != callback) {
                callback.call(block, info);
            }

            line.addView(block);
        }
    }

    public interface BlockCallback<T> {
        void call(ImageBlock block, T info);
    }
}
