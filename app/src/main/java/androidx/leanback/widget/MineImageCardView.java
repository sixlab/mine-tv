package androidx.leanback.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.reflect.Field;

public class MineImageCardView extends ImageCardView {

    public MineImageCardView(Context context, int themeResId) {
        super(context, themeResId);
    }

    public MineImageCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MineImageCardView(Context context) {
        super(context);
    }

    public MineImageCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setFont() {
        try {
            Field mTitleField = ImageCardView.class.getDeclaredField("mTitleView");
            Field mContentField = ImageCardView.class.getDeclaredField("mContentView");

            mTitleField.setAccessible(true);
            mContentField.setAccessible(true);

            TextView mTitleView = (TextView) mTitleField.get(this);
            mTitleView.setTextSize(10);

            TextView mContentView = (TextView) mContentField.get(this);
            mContentView.setTextSize(8);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
