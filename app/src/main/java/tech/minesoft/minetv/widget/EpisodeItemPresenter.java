package tech.minesoft.minetv.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.leanback.widget.Presenter;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.bean.UrlInfo;

public class EpisodeItemPresenter extends Presenter {
    private Context mContext;

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
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
