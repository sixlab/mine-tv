package com.ubtv66.minetv.page;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.Presenter;

import com.ubtv66.minetv.R;
import com.ubtv66.minetv.vo.UrlInfo;

/*
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an Button
 */
public class BlockPresenter extends Presenter {
    private static final String TAG = "CardPresenter";

    // private static final int CARD_WIDTH = 200; // 313;
    // private static final int CARD_HEIGHT = 100; // 176;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    private static int sViewedFontColor;
    // private Drawable mDefaultCardImage;

    private static void updateCardBackgroundColor(Button view, boolean selected) {
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor(color);
        // view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Log.d(TAG, "onCreateViewHolder");

        sDefaultBackgroundColor =
                ContextCompat.getColor(parent.getContext(), R.color.bl_blue);
        sSelectedBackgroundColor =
                ContextCompat.getColor(parent.getContext(), R.color.bl_cyan);
        sViewedFontColor =
                ContextCompat.getColor(parent.getContext(), R.color.bl_purple);
        /*
         * This template uses a default image in res/drawable, but the general case for Android TV
         * will require your resources in xhdpi. For more information, see
         * https://developer.android.com/training/tv/start/layouts.html#density-resources
         */

        Button button = new androidx.appcompat.widget.AppCompatButton(parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };

        button.setPadding(40, 18, 40, 18);
        button.setFocusable(true);
        button.setFocusableInTouchMode(true);

        updateCardBackgroundColor(button, false);
        return new ViewHolder(button);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        UrlInfo info = (UrlInfo) item;
        Button button = (Button) viewHolder.view;

        Log.d(TAG, "onBindViewHolder");

        button.setText(info.getItemName());
        if(info.isViewed()){
            button.setTextColor(sViewedFontColor);
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        Log.d(TAG, "onUnbindViewHolder");
        // viewHolder.view;
        // Remove references to images so that the garbage collector can free up memory
    }
}
