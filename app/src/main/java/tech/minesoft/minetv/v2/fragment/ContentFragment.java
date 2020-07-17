package tech.minesoft.minetv.v2.fragment;

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

import tech.minesoft.minetv.MineActivity;
import tech.minesoft.minetv.R;
import tech.minesoft.minetv.data.DbHelper;
import tech.minesoft.minetv.presenter.MinePresenterSelector;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.utils.ListUtils;
import tech.minesoft.minetv.utils.SizeUtils;
import tech.minesoft.minetv.v2.base.BaseLazyLoadFragment;
import tech.minesoft.minetv.v2.bean.Footer;
import tech.minesoft.minetv.v2.widgets.TabVerticalGridView;
import tech.minesoft.minetv.vo.VodInfo;
import tech.minesoft.minetv.widget.BlockContentPresenter;


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
            mPbLoading.setVisibility(View.VISIBLE);
            mVerticalGridView.setVisibility(View.INVISIBLE);

            List<VodInfo> list = null;
            switch (mCurrentTabCode) {
                case "TabStar":
                    list = DbHelper.loadStar(getActivity());
                    break;
                case "TabHistory":
                    list = DbHelper.loadHis(getActivity());
                    break;
            }

            if (null != list && list.size() > 0) {
                List<List> listList = ListUtils.splitList(list, 6);
                for (List<VodInfo> item : listList) {
                    addItem(item);
                }

                addFooter();
                mPbLoading.setVisibility(View.GONE);
                mVerticalGridView.setVisibility(View.VISIBLE);
            }
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

    private void addItem(List<VodInfo> infoList) {
        ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(new BlockContentPresenter());

        arrayObjectAdapter.addAll(0, infoList);
        ListRow listRow = new ListRow(arrayObjectAdapter);
        addWithTryCatch(listRow);
    }

    private void addFooter() {
        addWithTryCatch(new Footer());
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
            Log.e(TAG, "onChildViewHolderSelected: " + position);

            if (mVerticalGridView == null) {
                return;
            }
            Log.e(TAG, "onChildViewHolderSelected: " + "　isPressUp:" + mVerticalGridView.isPressUp()
                    + " isPressDown:" + mVerticalGridView.isPressDown());

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
