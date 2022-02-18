package tech.minesoft.minetv.v4app.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import tech.minesoft.minetv.R;

public class LayoutUtils {
    public static LinearLayout.LayoutParams lineLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    public static LinearLayout.LayoutParams centerLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    public static LinearLayout.LayoutParams btnLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private static Context context;

    public static void init(Context mContext) {
        context = mContext;

        int dimension = (int) mContext.getResources().getDimension(R.dimen.base_size_1x);
        lineLayout.setMargins(0, dimension, 0, 0);

        centerLayout.setMargins(0, dimension, 0, 0);
        centerLayout.gravity = Gravity.CENTER_HORIZONTAL;

        btnLayout.setMarginStart(dimension);
    }
}
