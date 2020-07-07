package com.ubtv66.minetv.page.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;
import androidx.leanback.app.SearchFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.SpeechRecognitionCallback;

import com.ubtv66.minetv.R;
import com.ubtv66.minetv.data.DbHelper;
import com.ubtv66.minetv.data.RequestHelper;
import com.ubtv66.minetv.page.CardPresenter;
import com.ubtv66.minetv.page.detail.VodDetailActivity;
import com.ubtv66.minetv.utils.ListUtils;
import com.ubtv66.minetv.utils.MineCallback;
import com.ubtv66.minetv.vo.VodInfo;
import com.ubtv66.minetv.vo.VodListVo;

import java.util.List;

public class MineSearchFragment extends SearchFragment
        implements SearchFragment.SearchResultProvider {

    private static final String TAG = MineSearchFragment.class.getSimpleName();

    private static final int REQUEST_SPEECH = 0x00000010;
    // private static final long SEARCH_DELAY_MS = 1000L;

    private ArrayObjectAdapter mRowsAdapter;

    private final Handler mHandler = new Handler();
    private final Runnable mDelayedLoad = new Runnable() {
        @Override
        public void run() {
            loadRows();
        }
    };
    private String mQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        setSearchResultProvider(this);
        setOnItemViewClickedListener(new ItemViewClickedListener());

        setSpeechRecognitionCallback(new SpeechRecognitionCallback() {
            @Override
            public void recognizeSpeech() {
                // Log.v(TAG, "recognizeSpeech");
                // try {
                //     startActivityForResult(getRecognizerIntent(), REQUEST_SPEECH);
                // } catch (ActivityNotFoundException e) {
                //     Log.e(TAG, "Cannot find activity for speech recognizer", e);
                // }
            }
        });
    }

    public boolean hasResults() {
        return mRowsAdapter.size() > 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult requestCode=" + requestCode +
                " resultCode=" + resultCode +
                " data=" + data);

        switch (requestCode) {
            case REQUEST_SPEECH:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        setSearchQuery(data, true);
                        break;
                    case RecognizerIntent.RESULT_CLIENT_ERROR:
                        Log.w(TAG, Integer.toString(requestCode));
                }
        }
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        Log.d(TAG, "getResultsAdapter");
        // mRowsAdapter (Search result) has prepared in loadRows method
        return mRowsAdapter;
    }

    @Override
    public boolean onQueryTextChange(String newQuery){
        Log.i(TAG, String.format("Search Query Text Change %s", newQuery));
        // loadQueryWithDelay(newQuery, SEARCH_DELAY_MS);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i(TAG, String.format("Search Query Text Submit %s", query));
        // No need to delay(wait) loadQuery, since the query typing has completed.
        loadQueryWithDelay(query, 0);
        return true;
    }

    /**
     * Starts {@link #loadRows()} method after delay.
     * It also cancels previously registered task if it has not yet executed.
     * @param query the word to be searched
     * @param delay the time to wait until loadRows will be executed (milliseconds).
     */
    private void loadQueryWithDelay(String query, long delay) {
        mHandler.removeCallbacks(mDelayedLoad);
        if (!TextUtils.isEmpty(query) && !query.equals("nil")) {
            mQuery = query;
            mHandler.postDelayed(mDelayedLoad, delay);
        }
    }

    /**
     * Searches query specified by mQuery, and sets the result to mRowsAdapter.
     */
    private void loadRows() {
        // offload processing from the UI thread
        RequestHelper.service.detail(mQuery,1).enqueue(new MineCallback<VodListVo>() {
            @Override
            public void success(VodListVo body) {
                mRowsAdapter.clear();

                List<VodInfo> list = body.getList();

                if(null!=list && list.size()>0){
                    List<List> listList = ListUtils.splitList(list, 6);

                    for (List itemList : listList) {
                        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
                        listRowAdapter.addAll(0, itemList);
                        ListRow listRow = new ListRow(listRowAdapter);
                        mRowsAdapter.add(listRow);
                    }

                    HeaderItem header = new HeaderItem(getString(R.string.search_results, list.size() + ""));
                    ListRow row = new ListRow(header, new ArrayObjectAdapter());
                    mRowsAdapter.add(row);
                }else{
                    HeaderItem header = new HeaderItem(getString(R.string.search_results_none, mQuery));
                    ListRow row = new ListRow(header, new ArrayObjectAdapter());
                    mRowsAdapter.add(row);
                }
            }
        });
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof VodInfo) {
                VodInfo info = (VodInfo) item;

                DbHelper.insertHis(getContext(), info);

                Log.d(TAG, "Movie: " + info.toString());
                Intent intent = new Intent(getActivity(), VodDetailActivity.class);
                intent.putExtra(VodDetailActivity.MOVIE, info);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        VodDetailActivity.SHARED_ELEMENT_NAME).toBundle();

                getActivity().startActivity(intent, bundle);
            } else {
                Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    // @Override
    // public void onPause() {
    //     try{
    //         Class<?> clazz=this.getClass().getSuperclass();
    //         Field field=clazz.getDeclaredField("mSpeechRecognizer");
    //         field.setAccessible(true);
    //         field.set(this,null);
    //
    //         super.onPause();
    //     }catch (Exception e){
    //         e.printStackTrace();
    //     }
    // }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}