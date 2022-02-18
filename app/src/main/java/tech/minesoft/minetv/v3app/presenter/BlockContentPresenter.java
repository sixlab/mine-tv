package tech.minesoft.minetv.v3app.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import tech.minesoft.minetv.v3app.R;
import tech.minesoft.minetv.v3app.activity.V3DetailActivity;
import tech.minesoft.minetv.v3app.base.BaseLazyLoadFragment;
import tech.minesoft.minetv.v3app.bean.MineMovieInfo;
import tech.minesoft.minetv.v3app.greendao.V3DaoHelper;
import tech.minesoft.minetv.v3app.utils.Const;
import tech.minesoft.minetv.v3app.utils.SizeUtils;


public class BlockContentPresenter extends Presenter {
    private static final String TAG = "BlockContentPresenter";

    private Context mContext;
    private BaseLazyLoadFragment fragment;

    public BlockContentPresenter(Context mContext, BaseLazyLoadFragment fragment) {
        this.mContext = mContext;
        this.fragment = fragment;
    }

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.widget_block, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        if (item instanceof MineMovieInfo) {
            MineMovieInfo info = (MineMovieInfo) item;
            ViewHolder vh = (ViewHolder) viewHolder;
            Glide.with(mContext)
                    .load(info.getVod_pic())
                    .apply(new RequestOptions()
                            .override(SizeUtils.dp2px(mContext, 125),
                                    SizeUtils.dp2px(mContext, 185))
                            .placeholder(R.drawable.load))
                    .into(vh.mIvPic);

            vh.mTvTitle.setText(info.getVod_name());
            vh.mTvDesc.setText(String.format("%s:%s", info.getVod_year(), info.getVod_director()));

            vh.view.setOnFocusChangeListener((v, hasFocus) -> {
                int color;
                if (hasFocus) {
                    color = mContext.getColor(R.color.bl_blue);
                    if (fragment != null) {
                        fragment.showText(info.getVod_name());
                    }
                } else {
                    color = mContext.getColor(R.color.bl_transparent);
                }
                v.setBackgroundColor(color);
            });

            vh.view.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if(TextUtils.isEmpty(info.getVod_play_url())){
                                showText("播放地址为空");
                                return true;
                            }
                            long infoId = V3DaoHelper.saveInfo(info);

                            Intent intent = new Intent(mContext, V3DetailActivity.class);
                            intent.putExtra(Const.SELECT_MOVIE_ID, infoId);

                            mContext.startActivity(intent);
                            return true;
                        case KeyEvent.KEYCODE_MENU:
                            if (fragment != null) {
                                fragment.delItem(info);
                                return true;
                            }
                    }
                }

                return false;
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
            toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }

    public static class ViewHolder extends Presenter.ViewHolder {

        private final ImageView mIvPic;
        private final TextView mTvTitle;
        private final TextView mTvDesc;

        public ViewHolder(View view) {
            super(view);
            mIvPic = view.findViewById(R.id.iv_pic);
            mTvTitle = view.findViewById(R.id.tv_title);
            mTvDesc = view.findViewById(R.id.tv_desc);
        }
    }
}
