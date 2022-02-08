package tech.minesoft.minetv.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.OnChildViewHolderSelectedListener;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.activity.MineActivity;
import tech.minesoft.minetv.base.BaseLazyLoadFragment;
import tech.minesoft.minetv.bean.MineMovieInfo;
import tech.minesoft.minetv.bean.MineSiteInfo;
import tech.minesoft.minetv.greendao.V3DaoHelper;
import tech.minesoft.minetv.presenter.BlockContentPresenter;
import tech.minesoft.minetv.presenter.MinePresenterSelector;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.utils.ListUtils;
import tech.minesoft.minetv.utils.MineCallback;
import tech.minesoft.minetv.utils.RetrofitHelper;
import tech.minesoft.minetv.utils.RetrofitService;
import tech.minesoft.minetv.utils.SizeUtils;
import tech.minesoft.minetv.vo.MovieListVo;
import tech.minesoft.minetv.widget.TabVerticalGridView;


public class SearchFragment extends BaseLazyLoadFragment {
    private static final String TAG = "SearchFragment";

    private TabVerticalGridView mVerticalGridView;
    private MineActivity mActivity;
    private View mRootView;

    private ProgressBar mPbLoading;
    private ArrayObjectAdapter mAdapter;

    private OnFragmentInteractionListener mListener;

    private String keyword;
    private Integer page = 0;
    private Integer totalPage = 0;

    public static SearchFragment newInstance() {
        Log.e(TAG, "new Instance status:  search ");

        return new SearchFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mActivity = (MineActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_search, container, false);
            initView();
            initListener();
        }
        return mRootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initView() {
        mPbLoading = mRootView.findViewById(R.id.pb_loading);
        mVerticalGridView = mRootView.findViewById(R.id.hg_content);

        mVerticalGridView.setTabView(mActivity.getHorizontalGridView());
        mVerticalGridView.setGroup(mActivity.getGroup());
        mVerticalGridView.setVerticalSpacing(SizeUtils.dp2px(mActivity, 24));

        MinePresenterSelector presenterSelector = new MinePresenterSelector();
        mAdapter = new ArrayObjectAdapter(presenterSelector);
        ItemBridgeAdapter itemBridgeAdapter = new ItemBridgeAdapter(mAdapter);
        mVerticalGridView.setAdapter(itemBridgeAdapter);

        addWithTryCatch(MinePresenterSelector.Selector.newInstance(Const.PRESENTER_SEARCH, this));
    }

    private void initListener() {
        mVerticalGridView.addOnScrollListener(onScrollListener);
        mVerticalGridView.addOnChildViewHolderSelectedListener(onSelectedListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mVerticalGridView != null) {
            mVerticalGridView.removeOnScrollListener(onScrollListener);
            mVerticalGridView.removeOnChildViewHolderSelectedListener(onSelectedListener);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(TAG, " isVisibleToUser:" + isVisibleToUser);
        if (!isVisibleToUser) {
            scrollToTop();
        }
    }

    @Override
    public void fetchData() {

    }

    private void scrollToTop() {
        if (mVerticalGridView != null) {
            mVerticalGridView.scrollToPosition(0);
            //            currentTitleRequestFocus();
            if (mActivity.getGroup() != null && mActivity.getGroup().getVisibility() != View.VISIBLE) {
                mActivity.getGroup().setVisibility(View.VISIBLE);
            }
        }
    }

    private void addItem(List<MineMovieInfo> infoList, String title) {
        ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(new BlockContentPresenter(getContext(), this));

        arrayObjectAdapter.addAll(0, infoList);
        HeaderItem headerItem = null;
        if(!TextUtils.isEmpty(title)){
            headerItem = new HeaderItem(title);
        }
        ListRow listRow = new ListRow(headerItem, arrayObjectAdapter);
        addWithTryCatch(listRow);
    }

    private void addFooter() {
        addWithTryCatch(MinePresenterSelector.Selector.newInstance(Const.PRESENTER_PAGER, this));
    }

    private final RecyclerView.OnScrollListener onScrollListener
            = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    //由于用户的操作，屏幕产生惯性滑动，停止加载图片
                case RecyclerView.SCROLL_STATE_SETTLING:
                    Glide.with(mActivity).pauseRequests();
                    break;
                case RecyclerView.SCROLL_STATE_IDLE:
                    Glide.with(mActivity).resumeRequests();
            }
        }
    };

    private final OnChildViewHolderSelectedListener onSelectedListener
            = new OnChildViewHolderSelectedListener() {
        @Override
        public void onChildViewHolderSelected(RecyclerView parent,
                                              RecyclerView.ViewHolder child,
                                              int position, int subposition) {
            super.onChildViewHolderSelected(parent, child, position, subposition);
            if (mVerticalGridView == null) {
                return;
            }

            if (mVerticalGridView.isPressUp() && position == 0) {
                mListener.onFragmentInteraction(Uri.parse(Const.URI_SHOW_TITLE));
            } else if (mVerticalGridView.isPressDown() && position == 1) {
                mListener.onFragmentInteraction(Uri.parse(Const.URI_HIDE_TITLE));
            }
        }
    };

    private void addWithTryCatch(Object item) {
        try {
            if (!mVerticalGridView.isComputingLayout()) {
                mAdapter.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasPrev() {
        return page > 1;
    }

    public boolean hasNext() {
        return page < totalPage;
    }

    public void search(String query) {
        keyword = query;

        page = 0;
        totalPage = 0;

        searchNext();
    }

    public void searchPrev() {
        Log.e(TAG, " search next :" + page);

        searchPage(-1);
    }

    public void searchNext() {
        Log.e(TAG, " search next :" + page);

        searchPage(1);
    }

    private void searchPage(int step) {
        MineSiteInfo activeSite = V3DaoHelper.getPrimarySite();

        if (null != activeSite) {
            RetrofitService service = RetrofitHelper.get(activeSite.getCode());

            if (null != service) {
                mPbLoading.setVisibility(View.VISIBLE);

                service.detail(keyword, page + step).enqueue(new MineCallback<MovieListVo>(getContext()) {
                    @Override
                    public void finish(boolean success, MovieListVo body, String message) {
                        mPbLoading.setVisibility(View.GONE);
                        if (success) {
                            List<MineMovieInfo> list = body.getList();
                            if (null != list && list.size() > 0) {
                                totalPage = body.getPagecount();
                                page = Integer.valueOf(body.getPage());

                                for (MineMovieInfo item : list) {
                                    item.setApi_code(activeSite.getCode());
                                    item.setApi_name(activeSite.getName());
                                    item.setApi_url(activeSite.getUrl());
                                }

                                addResult(body.getTotal(), list);
                            } else {
                                mAdapter.clear();
                                addWithTryCatch(MinePresenterSelector.Selector.newInstance(Const.PRESENTER_SEARCH, SearchFragment.this));
                                addItem(new ArrayList<>(), getString(R.string.search_results_none, keyword));
                            }
                        }
                    }
                });
            }
        }
    }

    private void addResult(Integer total, List<MineMovieInfo> list){
        mAdapter.clear();
        addWithTryCatch(MinePresenterSelector.Selector.newInstance(Const.PRESENTER_SEARCH, SearchFragment.this));

        String title = getString(R.string.search_results, keyword, total, page, totalPage);
        List<List> listList = ListUtils.splitList(list, 5);
        for (List<MineMovieInfo> item : listList) {
            addItem(item, title);
            title = null;
        }

        addFooter();
    }
}
