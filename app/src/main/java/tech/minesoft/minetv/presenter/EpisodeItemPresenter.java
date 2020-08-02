package tech.minesoft.minetv.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.leanback.widget.Presenter;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.activity.DetailActivity;
import tech.minesoft.minetv.activity.PlayerActivity;
import tech.minesoft.minetv.greendao.DaoHelper;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.vo.UrlInfo;

public class EpisodeItemPresenter extends Presenter {
    private Context mContext;

    public EpisodeItemPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_episode, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        if (item instanceof UrlInfo) {
            UrlInfo urlInfo = (UrlInfo) item;
            ViewHolder vh = (ViewHolder) viewHolder;
            vh.mTvEpisodeName.setText(urlInfo.getItemName());
            int color = urlInfo.isViewed() ? R.color.bl_purple : R.color.colorWhite;
            vh.mTvEpisodeName.setTextColor(mContext.getColor(color));

            vh.view.setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            DaoHelper.addView(urlInfo);

                            Intent intent = new Intent(mContext, PlayerActivity.class);
                            intent.putExtra(Const.SELECT_EPISODE, urlInfo);

                            mContext.startActivity(intent);
                            return true;
                        case KeyEvent.KEYCODE_MENU:
                            if (mContext instanceof DetailActivity) {
                                DetailActivity activity = (DetailActivity) mContext;
                                activity.clean(urlInfo);
                                return true;
                            }
                    }
                }

                return false;
            });
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }

    public static class ViewHolder extends Presenter.ViewHolder {

        private TextView mTvEpisodeName;

        ViewHolder(View view) {
            super(view);
            mTvEpisodeName = view.findViewById(R.id.tv_episode_name);
        }
    }
}
