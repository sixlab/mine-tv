package tech.minesoft.minetv.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.leanback.widget.Presenter;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.fragment.SearchFragment;

public class SearchPresenter extends Presenter {
    private Context mContext;

    private static final String TAG = "TypeFooterPresenter";

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.widget_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        if (viewHolder instanceof ViewHolder) {
            SearchPresenter.ViewHolder vh = (ViewHolder) viewHolder;
            vh.mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    vh.mSearch.setIconified(true);

                    if (item instanceof SearchFragment) {
                        SearchFragment sf = (SearchFragment) item;
                        sf.search(query);
                    }


                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

    }

    public static class ViewHolder extends Presenter.ViewHolder {
        private final SearchView mSearch;

        public ViewHolder(View view) {
            super(view);
            mSearch = view.findViewById(R.id.search_input);
        }
    }
}
