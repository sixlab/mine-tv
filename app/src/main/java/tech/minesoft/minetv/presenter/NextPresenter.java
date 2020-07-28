package tech.minesoft.minetv.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.leanback.widget.Presenter;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.fragment.SearchFragment;
import tech.minesoft.minetv.widget.ScaleConstraintLayout;

public class NextPresenter extends Presenter {
    private static final String TAG = "FooterPresenter";

    private Context mContext;

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.presenter_next, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        if (viewHolder instanceof NextPresenter.ViewHolder) {
            NextPresenter.ViewHolder vh = (NextPresenter.ViewHolder) viewHolder;

            MinePresenterSelector.Selector ms = (MinePresenterSelector.Selector) item;
            SearchFragment sf = (SearchFragment) ms.getItem();

            if(sf.hasPrev()){
                vh.mClPrev.setVisibility(View.VISIBLE);

                vh.mClPrev.setOnClickListener(v -> {
                    sf.searchPrev();
                });
            }else{
                vh.mClPrev.setVisibility(View.GONE);
            }

            if (sf.hasNext()) {
                vh.mClNext.setVisibility(View.VISIBLE);

                vh.mClNext.setOnClickListener(v -> {
                    sf.searchNext();
                });
            } else {
                vh.mClNext.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }

    public static class ViewHolder extends Presenter.ViewHolder {
        private final ScaleConstraintLayout mClPrev;
        private final ScaleConstraintLayout mClNext;

        public ViewHolder(View view) {
            super(view);
            mClPrev = view.findViewById(R.id.cl_prev);
            mClNext = view.findViewById(R.id.cl_next);
        }
    }
}
