package tech.minesoft.minetv.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.OnChildViewHolderSelectedListener;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import tech.minesoft.minetv.R;
import tech.minesoft.minetv.activity.MineActivity;
import tech.minesoft.minetv.base.BaseLazyLoadFragment;
import tech.minesoft.minetv.bean.MineMovieInfo;
import tech.minesoft.minetv.greendao.DaoHelper;
import tech.minesoft.minetv.presenter.BlockContentPresenter;
import tech.minesoft.minetv.presenter.MinePresenterSelector;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.utils.ListUtils;
import tech.minesoft.minetv.utils.SizeUtils;
import tech.minesoft.minetv.widget.TabVerticalGridView;


public class ContentFragment extends BaseLazyLoadFragment {
    private static final String TAG = "ContentFragment";

    private static final String BUNDLE_KEY_TAB_CODE = "bundleKeyTabCode";

    private TabVerticalGridView mVerticalGridView;
    private MineActivity mActivity;
    private View mRootView;
    private ProgressBar mPbLoading;
    private ArrayObjectAdapter mAdapter;

    private String mCurrentTabCode;

    private OnFragmentInteractionListener mListener;

    public static ContentFragment newInstance(int position, String tabCode) {
        Log.e(TAG + " pos:" + position, "new Instance status: " + position + " tab:" + tabCode);
        ContentFragment fragment = new ContentFragment();

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_TAB_CODE, tabCode);
        fragment.setArguments(bundle);

        return fragment;
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
    public void onResume() {
        super.onResume();
        fetchData();
    //    todo 好像是因为Resume的时候，界面处理有问题，才导致有时候顶部显示有问题
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG + " pos:", "onCreate: ");
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mCurrentTabCode = getArguments().getString(BUNDLE_KEY_TAB_CODE);
        Log.e(TAG, " tabCode: " + mCurrentTabCode);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_content, container, false);
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
        if (mCurrentTabCode != null) {
            // mPbLoading.setVisibility(View.VISIBLE);
            // mVerticalGridView.setVisibility(View.INVISIBLE);

            List<MineMovieInfo> list = null;
            switch (mCurrentTabCode) {
                case Const.TAB_RECORD:
                    list = DaoHelper.loadAll();
                    break;
                case Const.TAB_STAR:
                    list = DaoHelper.loadStar();
                    break;
                case Const.TAB_OTHER:
                    list = DaoHelper.loadUnStar();
                    break;
            }

            addItem(list);
        }
    }

    @Override
    public void delItem(MineMovieInfo info) {
        Context mContext = getContext();
        switch (mCurrentTabCode) {
            case Const.TAB_STAR:
                new AlertDialog.Builder(mContext)
                        .setMessage("是否取消收藏《" + info.getVod_name() + "》？")
                        .setNegativeButton("确定", (dialog, id) -> {
                            DaoHelper.changeStar(info.getId());
                            showText("取消成功");
                            fetchData();
                        })
                        .setPositiveButton("取消", null)
                        .show();
                break;
            case Const.TAB_RECORD:
            case Const.TAB_OTHER:
                new AlertDialog.Builder(mContext)
                        .setMessage("是否删除记录《" + info.getVod_name() + "》？")
                        .setNegativeButton("确定", (dialog, id) -> {
                            DaoHelper.delInfo(info.getId());
                            showText("删除成功");
                            fetchData();
                        })
                        .setPositiveButton("取消", null)
                        .show();
                break;
        }
    }

    //    public boolean onKeyEvent(KeyEvent keyEvent) {
    //        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
    //
    //
    //        }
    //        return false;
    //    }

    private void scrollToTop() {
        if (mVerticalGridView != null) {
            mVerticalGridView.scrollToPosition(0);
            // currentTitleRequestFocus();
            if (mActivity.getGroup() != null && mActivity.getGroup().getVisibility() != View.VISIBLE) {
                mActivity.getGroup().setVisibility(View.VISIBLE);
            }
        }
    }

    private void addItem(List<MineMovieInfo> list) {
        mAdapter.clear();

        if (null != list && list.size() > 0) {
            List<List> listList = ListUtils.splitList(list, 5);
            for (List<MineMovieInfo> item : listList) {
                BlockContentPresenter presenter = new BlockContentPresenter(getContext(), this);
                ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(presenter);

                arrayObjectAdapter.addAll(0, item);
                ListRow listRow = new ListRow(arrayObjectAdapter);
                addWithTryCatch(listRow);
            }

            addFooter();
            // mPbLoading.setVisibility(View.GONE);
            // mVerticalGridView.setVisibility(View.VISIBLE);
        }
    }

    private void addFooter() {
        addWithTryCatch(MinePresenterSelector.Selector.newInstance(Const.PRESENTER_FOOTER, this));
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
}
