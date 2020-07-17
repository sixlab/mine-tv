package tech.minesoft.minetv.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.RowHeaderPresenter;
import androidx.leanback.widget.RowPresenter;

import tech.minesoft.minetv.PlayerActivity;
import tech.minesoft.minetv.R;
import tech.minesoft.minetv.data.DbHelper;
import tech.minesoft.minetv.utils.Const;
import tech.minesoft.minetv.utils.SizeUtils;
import tech.minesoft.minetv.v2.base.BaseListRowPresenter;
import tech.minesoft.minetv.vo.UrlInfo;


public class ListRowPresenter extends BaseListRowPresenter {
    @SuppressLint("RestrictedApi")
    @Override
    protected void initializeRowViewHolder(RowPresenter.ViewHolder holder) {
        super.initializeRowViewHolder(holder);
        final ViewHolder rowViewHolder = (ViewHolder) holder;

        rowViewHolder.getGridView().setHorizontalSpacing(SizeUtils.dp2px(rowViewHolder.getGridView().getContext(), 24));
        rowViewHolder.getGridView().setFocusScrollStrategy(HorizontalGridView.FOCUS_SCROLL_ITEM);
        RowHeaderPresenter.ViewHolder vh = rowViewHolder.getHeaderViewHolder();
        TextView textView = vh.view.findViewById(R.id.row_header);
        textView.setTextSize(SizeUtils.dp2px(textView.getContext(), 15));
        textView.setTextColor(textView.getContext().getResources().getColor(R.color.bl_blue));
        textView.setPadding(0, 0, 0, 20);
        setOnItemViewClickedListener((itemViewHolder, item, rowViewHolder1, row) -> {
            Context context = itemViewHolder.view.getContext();

            if (item instanceof UrlInfo) {
                UrlInfo urlInfo = (UrlInfo) item;

                DbHelper.addView(context, urlInfo);

                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra(Const.SELECT_EPISODE, urlInfo);

                context.startActivity(intent);
            }
        });
    }
}
