package com.ubtv66.minetv.page.detail;

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.ubtv66.minetv.vo.VodInfo;

import java.text.MessageFormat;

public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    private static final int HEIGHT = 100;

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        VodInfo info = (VodInfo) item;

        if (info != null) {
            viewHolder.getTitle().setText(MessageFormat.format("[{0}]{1}{2}", info.getVod_year(), info.getVod_name(),info.getType_name()));
            viewHolder.getSubtitle().setText(MessageFormat.format("导演：{0}\n演员：{1}", info.getVod_director(), info.getVod_actor()));
            viewHolder.getBody().setText(info.getVod_content());
        }

        // viewHolder.getBody().setHeight(HEIGHT);
    }
}
