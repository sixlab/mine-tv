package com.ubtv66.minetv.page.detail;

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.ubtv66.minetv.vo.VodInfo;

import java.text.MessageFormat;

public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        VodInfo info = (VodInfo) item;

        if (info != null) {
            viewHolder.getTitle().setText(info.getVod_name());
            viewHolder.getSubtitle().setText(info.getVod_year());
            viewHolder.getBody().setText(
                    MessageFormat.format(
                            "类型：{0}\n导演：{1}\n演员：{2}\n{3}",
                            info.getType_name(), info.getVod_director(), info.getVod_actor(), info.getVod_content()
                    )
            );
        }
    }
}
