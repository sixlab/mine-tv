package tech.minesoft.minetv.v3app.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.leanback.widget.Presenter;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.v3app.widget.TabVerticalGridView;

public class FooterPresenter extends Presenter {
    private static final String TAG = "FooterPresenter";

    private Context mContext;

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.presenter_footer, parent, false);
        view.findViewById(R.id.cl_back_to_top)
                .setOnClickListener(v -> {
                    if (v.getParent().getParent() instanceof TabVerticalGridView) {
                        ((TabVerticalGridView) v.getParent().getParent()).backToTop();
                    }
                });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {

    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }

    public static class ViewHolder extends Presenter.ViewHolder {
        private final TextView mIvBackToTop;

        public ViewHolder(View view) {
            super(view);
            mIvBackToTop = view.findViewById(R.id.tv_back_to_top);
        }
    }
}
