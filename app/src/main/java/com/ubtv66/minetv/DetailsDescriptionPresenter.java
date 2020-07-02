package com.ubtv66.minetv;

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.ubtv66.minetv.vo.VodInfo;

public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        VodInfo info = (VodInfo) item;

        if (info != null) {
            viewHolder.getTitle().setText(info.getVodName());
            viewHolder.getSubtitle().setText(info.getVodName());
            viewHolder.getBody().setText(info.getVodName());
        }
    }
}
