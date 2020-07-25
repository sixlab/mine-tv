package tech.minesoft.minetv.presenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.leanback.widget.Presenter;

import tech.minesoft.minetv.R;


public class EpisodeGroupPresenter extends Presenter {

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.widget_episode_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        if (item instanceof String) {
            ViewHolder vh = (ViewHolder) viewHolder;
            vh.mTvGroupName.setText(item.toString());
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }

    public static class ViewHolder extends Presenter.ViewHolder {

        private TextView mTvGroupName;

        ViewHolder(View view) {
            super(view);
            mTvGroupName = view.findViewById(R.id.tv_group_name);
        }
    }
}
