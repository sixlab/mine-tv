package tech.minesoft.minetv.v4app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import tech.minesoft.minetv.v4app.R;

public class IconButton extends AppCompatImageButton {
    private Context context;

    public IconButton(@NonNull Context context) {
        super(context);
        this.init(context, null, 0);
    }

    public IconButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs, 0);
    }

    public IconButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        setNormalStyle();
        setScaleType(ScaleType.CENTER_INSIDE);
        setOnFocusChangeListener(this::onFocusChange);
    }

    public void onFocusChange(View view, boolean focus) {
        if (focus) {
            setFocusStyle();
        } else {
            setNormalStyle();
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }

    private void setFocusStyle() {
        setBackgroundColor(context.getColor(R.color.mtv_selected));
    }

    private void setNormalStyle() {
        setBackgroundColor(context.getColor(R.color.mtv_btn_normal));
    }
}
